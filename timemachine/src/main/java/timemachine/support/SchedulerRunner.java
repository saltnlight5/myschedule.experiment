package timemachine.support;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.DataStore;
import timemachine.DateUtil;
import timemachine.Job;
import timemachine.JobContext;
import timemachine.Schedule;
import timemachine.Scheduler;



public class SchedulerRunner implements Runnable {
	public static final long MAX_SCHEDULER_CHECK_INTERVAL = DateUtil.MILLIS_IN_HOUR;
	public static final long MIN_SCHEDULER_CHECK_INTERVAL = DateUtil.MILLIS_IN_SECOND;
	private static Logger logger = LoggerFactory.getLogger(SchedulerRunner.class);
	private long maxSchedulerCheckInterval = MAX_SCHEDULER_CHECK_INTERVAL;
	private long minSchedulerCheckInterval = MIN_SCHEDULER_CHECK_INTERVAL;
	private long schedulerCheckInterval = MAX_SCHEDULER_CHECK_INTERVAL;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean paused = new AtomicBoolean(false);
	private Scheduler scheduler;
	int batchSize;
	long batchWindowsInMillis;
	private ExecutorService jobThreadPool;
	
	public void setSchedulerCheckInterval(long schedulerCheckInterval) {
		this.schedulerCheckInterval = schedulerCheckInterval;
	}
	public void setMaxSchedulerCheckInterval(long maxSchedulerCheckInterval) {
		this.maxSchedulerCheckInterval = maxSchedulerCheckInterval;
	}
	public void setMinSchedulerCheckInterval(long minSchedulerCheckInterval) {
		this.minSchedulerCheckInterval = minSchedulerCheckInterval;
	}
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	public void setJobThreadPool(ExecutorService jobThreadPool) {
		this.jobThreadPool = jobThreadPool;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public void setBatchWindowsInMillis(long batchWindowsInMillis) {
		this.batchWindowsInMillis = batchWindowsInMillis;
	}	
	
	public void wakeUpSleepCycle() {
		// Await any blocking instances
		synchronized(this) {
			notifyAll(); // We typically will only have one runner, but just in case.
		}
	}
	
	public boolean isRunning() {
		return running.get();
	}
	
	public boolean isPaused() {
		return paused.get();
	}
	
	public void pause() {
		paused.set(true);
	}
	
	public void resume() {
		paused.set(false);
		wakeUpSleepCycle();
	}
	
	public void stop() {
		running.set(false);
		wakeUpSleepCycle();
	}
	
	@Override
	public void run() {
		// reset flags in case we re-run with the same instance.
		paused.set(false);
		running.set(true);
		
		// run scheduler check loop.
		while (running.get()) {
			if (!paused.get()) {
				checkScheduler();
			}
			sleepBetweenCheck();
		}
		logger.debug("Scheduler runner is done.");
	}
	
	private void sleepBetweenCheck() {
		try {
			synchronized(this) {
				// Use wait on this object to block instead of Thread.sleep(), because we may need to notify/wake it
				// in case there is DataStore change notification.
				logger.debug("Sleep between scheduler check for {}ms", schedulerCheckInterval);
				wait(schedulerCheckInterval);
			}
		} catch (InterruptedException e) {
			// If we can't sleep, we just continue anyway.
		}
	}

	protected void checkScheduler() {
		logger.debug("Checking scheduler for jobs to run.");
		DataStore dataStore = scheduler.getDataStore();
		Date cutoffTime = new Date(System.currentTimeMillis() + batchWindowsInMillis);
		List<Schedule> schedules = dataStore.getSchedulesToRun(batchSize, cutoffTime);
		for (Schedule schedule : schedules) {
			List<Job> jobs = schedule.getJobs();
			for (Job job : jobs) {
				logger.debug("Prepare to run {} with {}", job, schedule);
				runJob(job, schedule);
			}
		}

		// Adjust sleep cycle if necessary.
		Date earliestNextRunTime = dataStore.getEarliestRunTime();
		logger.debug("The earliestNextRunTime={} is used to update scheduler check internal.", earliestNextRunTime);
		updateSchedulerCheckInterval(earliestNextRunTime);
	}

	private void runJob(Job job, Schedule schedule) {
		JobContext jobContext = new JobContextImpl(scheduler, job, schedule);
		jobThreadPool.execute(new JobRunner(jobContext));
		logger.debug("{} added to thread pool for execution.", job);
	}

	private void updateSchedulerCheckInterval(Date earliestTime) {
		long gap = earliestTime.getTime() - System.currentTimeMillis();
		gap -= minSchedulerCheckInterval; // We want scheduler to start checking a little early
		
		// Note: careful not to use zero, b/c it will sleep forever!
		if (gap <= 0)
			gap = minSchedulerCheckInterval;
		
		if (gap < maxSchedulerCheckInterval) {
			schedulerCheckInterval = gap;
			return;
		}
		
		if (schedulerCheckInterval != maxSchedulerCheckInterval) {
			schedulerCheckInterval = maxSchedulerCheckInterval;
		}
	}
}

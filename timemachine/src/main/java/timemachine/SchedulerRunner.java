package timemachine;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchedulerRunner implements Runnable {
	public static final long MAX_SCHEDULER_CHECK_INTERVAL = DateUtil.MILLIS_IN_HOUR;
	private static Logger logger = LoggerFactory.getLogger(SchedulerRunner.class);
	private long schedulerCheckInterval = MAX_SCHEDULER_CHECK_INTERVAL;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean paused = new AtomicBoolean(false);
	private Scheduler scheduler;
	int batchSize= 1;
	long batchWindowsInMillis = DateUtil.MILLIS_IN_SECOND;
	private ExecutorService jobThreadPool;
	
	public SchedulerRunner(Scheduler scheduler, ExecutorService jobThreadPool) {
		this.scheduler = scheduler;
		this.jobThreadPool = jobThreadPool;
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
		Date earliestTime = dataStore.getEarliestRunTime();
		updateSchedulerCheckInterval(earliestTime);
	}

	private void runJob(Job job, Schedule schedule) {
		JobContext jobContext = new JobContextImpl(scheduler, job, schedule);
		jobThreadPool.execute(new JobRunner(jobContext));
		logger.debug("{} added to thread pool for execution.", job);
	}

	private void updateSchedulerCheckInterval(Date earliestTime) {
		long gap = earliestTime.getTime() - System.currentTimeMillis();
		gap -= batchWindowsInMillis - 1; // We want scheduler to start checking a little early
		if (gap <= 0)
			gap = 1; // This will ensure we do put sleep with 0, which is infinity, or any negative values (past due).
		
		if (gap < MAX_SCHEDULER_CHECK_INTERVAL) {
			schedulerCheckInterval = gap;
			return;
		} 
		if (schedulerCheckInterval != MAX_SCHEDULER_CHECK_INTERVAL) {
			schedulerCheckInterval = MAX_SCHEDULER_CHECK_INTERVAL;
		}
	}
	
	public static class JobRunner implements Runnable {
		private JobContext jobContext;
		public JobRunner(JobContext jobContext) {
			this.jobContext = jobContext;
		}
		@Override
		public void run() {
			Job job = jobContext.getJob();
			Class<? extends JobTask> jobTaskClass = job.getTaskClass();
			logger.debug("Creating jobTask instance " + jobTaskClass.getName());
			try {
				JobTask task = jobTaskClass.newInstance();
				logger.info("Running jobTask: " + task);
				task.run(jobContext);
			} catch (InstantiationException e) {
				throw new SchedulerException("Failed to create JobTask: " + jobTaskClass, e);
			} catch (IllegalAccessException e) {
				throw new SchedulerException("Failed to create JobTask: " + jobTaskClass, e);
			}
		}
		
	}
}

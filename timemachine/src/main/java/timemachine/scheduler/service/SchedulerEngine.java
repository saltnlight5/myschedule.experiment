package timemachine.scheduler.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.scheduler.Job;
import timemachine.scheduler.JobContext;
import timemachine.scheduler.Schedule;
import timemachine.scheduler.Scheduler;
import timemachine.scheduler.SchedulerException;
import timemachine.scheduler.Schedules;
import timemachine.scheduler.support.AbstractService;
import timemachine.scheduler.support.DataStore;
import timemachine.scheduler.support.JobContextImpl;
import timemachine.scheduler.support.JobRunner;
import timemachine.scheduler.support.Service;
import timemachine.scheduler.support.ThreadPool;



public class SchedulerEngine extends AbstractService implements Scheduler {
	public static final long DEFAULT_MAX_CHECK_INTERVAL = Schedules.MILLIS_IN_HOUR;
	public static final long DEFAULT_MIN_CHECK_INTERVAL = Schedules.MILLIS_IN_SECOND;
		
	// Don't forget super class also has "name" attribute that user may set!
	private ScheduleChecker scheduleChecker = new ScheduleChecker();
	private ServiceContainer userServices = new ServiceContainer();
	private ServiceContainer systemServices = new ServiceContainer();

	// If id is not given, it will try to auto generate one with dataStore.generateId
	private Long id;
	// Required fields
	private ThreadPool serviceThreadPool;
	private ThreadPool jobTaskThreadPool;
	private DataStore dataStore;
	
	public ScheduleChecker getScheduleChecker() {
		return scheduleChecker;
	}
	/**
	 * This should be the pool that will run ScheduleChecker, which will pull the dataStore for schedule to run.
	 * @param serviceThreadPool
	 */
	public void setServiceThreadPool(ThreadPool serviceThreadPool) {
		this.serviceThreadPool = serviceThreadPool;
	}
	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	public void setScheduleCheckerMaxInterval(long maxCheckInterval) {
		scheduleChecker.maxInterval = maxCheckInterval;
	}
	public void setScheduleCheckerMinInterval(long minCheckInterval) {
		scheduleChecker.minInterval = minCheckInterval;
	}
	public void setBatchSize(int batchSize) {
		scheduleChecker.batchSize = batchSize;
	}
	public void setBatchTimeWindow(long batchTimeWindow) {
		scheduleChecker.batchTimeWindow = batchTimeWindow;
	}
	public void setJobTaskThreadPool(ThreadPool jobTaskThreadPool) {
		this.jobTaskThreadPool = jobTaskThreadPool;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
		
	@Override
	public List<Service> getUserServices() {
		return userServices.getServices(); // it's an read-only list.
	}
	@Override
	public void addUserService(Service service) {
		userServices.addService(service);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + getId() +", name=" + getName() + "]";
	}
	
	// Scheduler API

	@Override
	public ThreadPool getServiceThreadPool() {
		return serviceThreadPool;
	}
	@Override
	public ThreadPool getJobTaskThreadPool() {
		return jobTaskThreadPool;
	}
	@Override
	public DataStore getDataStore() {
		return dataStore;
	}
	
	// Scheduler life cycles
	@Override
	public void initService() {
		systemServices.setName("SystemServiceContainer");
		userServices.setName("UserServiceContainer");
		
		if (id == null) {
			dataStore.init();
			id = dataStore.generateId(SchedulerEngine.class);
		}
		// Add all system services into a container to manage its life cycles.
		systemServices.addService(dataStore);
		systemServices.addService(serviceThreadPool);
		systemServices.addService(jobTaskThreadPool);
		systemServices.addService(userServices);
		
		// Init all services.
		logger.debug("Initializing {}", this);
		systemServices.init();
		logger.info("{} initialized.", this);
	}
	
	
	@Override
	public void startService() {
		logger.debug("Starting {}", this);
		systemServices.start();
		serviceThreadPool.execute(scheduleChecker);
		logger.info("{} started.", this);
	}

	@Override
	public void stopService() {
		logger.debug("Stopping {}", this);
		scheduleChecker.running.set(false);
		scheduleChecker.wakeUpSleepCycle();
		systemServices.stop();
		logger.info("{} stopped.", this);
	}

	@Override
	public void destroyService() {
		logger.debug("Destroying {}", this);
		systemServices.destroy();
		logger.info("{} destroyed.", this);
	}
	
	@Override
	public boolean isPaused() {
		return scheduleChecker.paused.get();
	}

	@Override
	public void pause() {
		scheduleChecker.paused.set(true);
	}

	@Override
	public void resume() {
		scheduleChecker.paused.set(false);
		scheduleChecker.wakeUpSleepCycle();
	}
	
	@Override
	public void scheduleJob(Job job) {
		if (!isInited())
			throw new SchedulerException("Failed to schedule job: Scheduler has not be initialized yet.");
		if (job == null)
			throw new SchedulerException("Failed to schedule job: Job or schedule is not set.");
		
		if (job.getTaskClass() == null)
			throw new SchedulerException("Failed to schedule job: Job.taskClass is not set.");

		dataStore.storeData(job);
		for (Schedule schedule : job.getSchedules()) {
			dataStore.storeData(schedule);
			logger.info("{} is with {}. The nextRun={}", new Object[]{ job, schedule, schedule.getNextRun() });
		}
		
		// Update scheduler engine with latest change
		scheduleChecker.wakeUpSleepCycle();
	}

	@Override
	public void removeJob(Long jobId) {
		logger.debug("Removing job.id={}", jobId);
		dataStore.deleteData(jobId, Job.class);
	}	
	
	private class ScheduleChecker implements Runnable {
		Logger logger = LoggerFactory.getLogger(ScheduleChecker.class);
		AtomicBoolean running = new AtomicBoolean(false);
		AtomicBoolean paused = new AtomicBoolean(false);
		long maxInterval = DEFAULT_MAX_CHECK_INTERVAL;
		long minInterval = DEFAULT_MIN_CHECK_INTERVAL;
		long checkInterval = maxInterval;
		long batchTimeWindow = Schedules.MILLIS_IN_SECOND;
		int batchSize = 1;
		
		@Override
		public void run() {
			// reset flags in case we re-run with the same instance.
			paused.set(false);
			running.set(true);
			
			// run scheduler check loop.
			while (running.get()) {
				if (!paused.get()) {
					checkScheduleForJobTasks();
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
					logger.debug("Sleep between scheduler check for {}ms", checkInterval);
					wait(checkInterval);
				}
			} catch (InterruptedException e) {
				// If we can't sleep, we just continue anyway.
			}
		}
		
		public void wakeUpSleepCycle() {
			// Await any blocking instances
			synchronized(this) {
				notifyAll(); // We typically will only have one runner, but just in case.
			}
		}

		protected void checkScheduleForJobTasks() {
			logger.debug("Checking scheduler for jobs to run.");
			Date cutoffTime = new Date(System.currentTimeMillis() + batchTimeWindow);
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
			updateScheduleCheckerInterval(earliestNextRunTime);
		}

		private void runJob(Job job, Schedule schedule) {
			JobContext jobContext = new JobContextImpl(SchedulerEngine.this, job, schedule);
			jobTaskThreadPool.execute(new JobRunner(jobContext));
			logger.debug("{} added to thread pool for execution.", job);
		}

		private void updateScheduleCheckerInterval(Date earliestTime) {
			long gap = earliestTime.getTime() - System.currentTimeMillis();
			gap -= minInterval; // We want scheduler to start checking a little early
			
			// Note: careful not to use zero, b/c it will sleep forever!
			if (gap <= 0)
				gap = minInterval;
			
			if (gap < maxInterval) {
				checkInterval = gap;
				return;
			}
			
			if (checkInterval != maxInterval) {
				checkInterval = maxInterval;
			}
		}
	}
}

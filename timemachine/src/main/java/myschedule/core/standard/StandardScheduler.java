package myschedule.core.standard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import myschedule.core.Job;
import myschedule.core.JobSchedule;
import myschedule.core.JobStore;
import myschedule.core.Schedule;
import myschedule.core.Scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardScheduler implements Scheduler {
	
	private static final long SCHEDULER_THREAD_STOP_MAX_TIMEOUT = 3000L;

	private static final long MAX_NEXT_RUN_JOB_TIME = 100L;

	private static Logger logger = LoggerFactory.getLogger(StandardScheduler.class);
	
	private String name;
	
	/** A map of a jobName per Job instance that are to be run by this scheduler. */
	private Map<String, Job> jobInstances = new HashMap<String, Job>();
	
	private JobStore jobStore = new InMemoryJobStore();
	
	private ExecutorService jobExecutor;	

	private volatile boolean started;
	
	private volatile boolean initialized;
	
	private Thread schedulerThread;
	
	@Override	
	public void add(JobSchedule jobSchedule) {
		String jobName = jobSchedule.getJobName();
		Utils.assertNotEmpty(jobName, "Job name can not be empty.");
		logger.debug("Adding job " + jobName);
		
		Job job = jobSchedule.getJob();
		if (job != null) {
			logger.debug("Adding job instance directly to scheduler: " + job);
			jobInstances.put(jobName, job);
		} else {
			logger.debug("Adding job schedule " + jobSchedule + " into jobStore: " + jobStore);
			jobStore.save(jobSchedule);
		}
		String runTimeMsg = "";
		if (jobSchedule.getSchedule() != null) {
			runTimeMsg = " Next run time is: " + jobSchedule.getSchedule().getNextRunJobTime();
		}
		logger.info("Job " + jobName + " added." + runTimeMsg);
	}

	@Override
	public void start() {
		// initialize scheduler first
		init();
		
		// Check to not start twice
		if (started) {
			logger.warn("Can not start. Scheduler has already been started.");
			return;
		}
		started = true;
		
		// Starting scheduler
		logger.debug("Starting " + this);
		
		// Init+Start scheduler thread
		schedulerThread = new Thread(new SchedulerTask());
		schedulerThread.start();

		// Done
		logger.info(this + " started.");
	}

	@Override
	public void stop() {
		// Check to not stop before start
		if (!started)
			return;
		started = false;
		
		// Stopping scheduler
		logger.debug("Stopping " + this);
		
		// Wait for scheduler thread to stop, or interrupt it if max timeout has reached.
		long tstamp = System.currentTimeMillis();
		while (schedulerThread.isAlive() && 
				(System.currentTimeMillis() - tstamp) < SCHEDULER_THREAD_STOP_MAX_TIMEOUT) {
			Utils.sleep(300);
		}
		if (schedulerThread.isAlive())
			schedulerThread.interrupt();
		
		// Shutdown thread pool
		logger.debug("Stopping jobExecutor: " + jobExecutor);
		jobExecutor.shutdown();
		
		// Done
		logger.info(this + " stopped.");
	}

	@Override
	public void init() {
		// Check to not init twice
		if (initialized)
			return;
		initialized = true;

		// Initializing scheduler
		logger.debug("Initializing " + this);

		// Init thread pool if not set
		if (jobExecutor == null) {
			logger.debug("Default jobExecutor to 2 threads pool.");
			jobExecutor = Executors.newFixedThreadPool(2);
		}
		
		// Init jobStore
		jobStore.init();
		
		// Done
		logger.info(this + " is ready.");
	}

	@Override
	public void destroy() {
		// Stop scheduler
		stop();
		
		// Check to not destroy non initialized scheduler
		if (!initialized)
			return;
		initialized = false;
		
		// Destroying scheduler
		logger.debug("Destroying " + this);
		
		// Destroying jobStore
		jobStore.destroy();
		
		// Done
		logger.info(this + " is done.");
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	@Override
	public String toString() {
		return "StandardScheduler[name=" + name + ", hashCode=" + System.identityHashCode(this) + "]";
	}
	
	private long checkRunJobSchedules() {
		//logger.debug("Checking run job schedules.");
		long nextCheckDelayTime = 1000;
		List<JobSchedule> jobSchedules = getNextRunJobSchedules();
		for (JobSchedule jobSchedule : jobSchedules) {
			updateRunJobStart(jobSchedule);
			try {
				runJobSchedule(jobSchedule);
			} catch (Exception e) {
				updateRunJobFailed(jobSchedule, e);
			}
			updateRunJobStop(jobSchedule);
		}
		return nextCheckDelayTime;
	}
	
	private void updateRunJobStop(JobSchedule jobSchedule) {
		//jobStore.updateRunJobStop(jobSchedule);
	}

	private void updateRunJobFailed(JobSchedule jobSchedule, Exception e) {
		//jobStore.updateRunJobFailed(jobSchedule, e);
	}

	private void updateRunJobStart(JobSchedule jobSchedule) {
		//jobStore.updateRunJobStart(jobSchedule);
	}

	private List<JobSchedule> getNextRunJobSchedules() {
		return jobStore.getNextRunJobSchedules(MAX_NEXT_RUN_JOB_TIME);
	}

	private void runJobSchedule(JobSchedule jobSchedule) {
		Job job = jobSchedule.getJob();
		if (job != null) {
			StandardRunJobContext runJobContext = new StandardRunJobContext(jobSchedule);
			job.execute(runJobContext);
		}
	}

	private class SchedulerTask implements Runnable {
		
		@Override
		public void run() {
			while (started) {
				long nextCheckDelayTime = checkRunJobSchedules();
				Utils.sleep(nextCheckDelayTime);
			}
		}
	}
}

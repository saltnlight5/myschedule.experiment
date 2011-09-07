package tim.scheduler;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimScheduler extends AbstractService implements Scheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(TimScheduler.class);
	protected SchedulerConfig config;			
	protected String name;
	protected Store store;
	protected Executor jobExecutionThreadPool;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void addSchedule(Schedule schedule) {
		logger.debug("Initializing schedule {} before adding to store.", schedule);
		schedule.init();
		store.addSchedule(schedule);
		logger.info("Schedule {} has been added to store.", schedule);
	}
	
	@Override
	public String toString() {
		return "TimScheudler[" + name + "]";
	}

	@Override
	public void initService() {
		if (config == null)
			config = SchedulerConfig.DEFAULT_CONFIG;
		if (name == null) 
			name = config.schedulerName;
		if (store == null)
			store = createInstance(config.storeClass);
		if (jobExecutionThreadPool == null)
			jobExecutionThreadPool = Executors.newFixedThreadPool(config.jobExecutionThreadPoolSize);
		
		logger.info("{} initialized.", this);
	}
	
	private <T> T createInstance(Class<T> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void destroyService() {
		logger.info("{} destroyed.", this);
	}
	
	@Override
	public void startService() {
		logger.info("{} started.", this);
		while(started.get()) {
			for (Schedule schedule : store.getSchedules()) {
				String name = schedule.getName();
				
				if(schedule.isEnded()) {
					logger.debug("Removing ended schedule {} from store.", name);
					store.removeSchedule(schedule);
					logger.debug("Schedule {} has been ended and removed from store.", name);
					continue;
				}

				Date nextRunTime = schedule.getNextRunTime();
				logger.debug("Checking schedule {} with nextRunTime {}.", name, nextRunTime);
				Date now = new Date();
				if (now.getTime() > nextRunTime.getTime()) {
					logger.debug("Jobs are due on schedule {}.", name);
					for (Job job : schedule.getJobs()) {
						poolJobForExecution(job);
					}
					schedule.updateNextRunTime();
				}
			}
			sleep(500);
		}
	}
	
	@Override
	public void stopService() {
		logger.info("{} stopped.", this);
	}

	protected void poolJobForExecution(Job job) {
		logger.debug("Preparing to pool job {} for execution.", job);
		if (job instanceof RunnableJob) {
			RunnableJob runnableJob = (RunnableJob)job;
			Class<? extends Runnable> jobClass = runnableJob.getRunabbleClass();
			Runnable jobInstance = createInstance(jobClass);
			jobExecutionThreadPool.execute(jobInstance);
		} else {
			throw new RuntimeException("Job type " + job.getClass().getName() + " has not yet implemented.");
		}
		logger.info("Job {} has been pooled for execution.", job);
	}
	
	private void sleep(long millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			throw new RuntimeException("Failed to sleep full " + millisecs + " millisecs.", e);
		}
	}
}

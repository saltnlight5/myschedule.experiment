package deng.timemachine;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerImpl extends AbstractService implements Scheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerImpl.class);
	protected SchedulerConfig config;			
	protected String name;
	protected Store store;
	protected ExecutorService workThreadPool;
	protected ExecutorService schedulerThreadPool;
	protected SchedulerRunner schedulerRunner;
	protected boolean shutdownThreadPoolImmediatly;
		
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
		if (workThreadPool == null)
			workThreadPool = Executors.newFixedThreadPool(config.workThreadPoolSize);
		if (schedulerThreadPool == null)
			schedulerThreadPool = Executors.newFixedThreadPool(1);
		if (schedulerRunner == null)
			schedulerRunner = new SchedulerRunner();
		
		shutdownThreadPoolImmediatly = config.shutdownThreadPoolImmediatly;
		
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
		schedulerThreadPool.execute(schedulerRunner);
		logger.info("{} started.", this);
	}
	
	@Override
	public void stopService() {
		if (shutdownThreadPoolImmediatly) {
			logger.debug("Shutting down thread pools now.");
			workThreadPool.shutdownNow();
			schedulerThreadPool.shutdownNow();
		} else {
			logger.debug("Shutting down thread pools now.");
			workThreadPool.shutdown();
			schedulerThreadPool.shutdown();
		}
		logger.info("{} stopped.", this);
	}

	protected void poolJobForExecution(Job job) {
		logger.debug("Preparing to pool job {} work for execution.", job);
		
		Class<? extends Task> workClass = job.getWorkClass();
		Task work = createInstance(workClass);
		WorkRunner workRunner = new WorkRunner(work);
		workThreadPool.execute(workRunner);
		
		logger.info("Job {} work has been pooled for execution.", job);
	}
	
	private void sleep(long millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			throw new RuntimeException("Failed to sleep full " + millisecs + " millisecs.", e);
		}
	}
	
	public class WorkRunner implements Runnable {

		protected Task work;
		
		public WorkRunner(Task work) {
			this.work = work;
		}
		
		@Override
		public void run() {
			SchedulerContext schedulerCtx = new SchedulerContextImpl(SchedulerImpl.this);
			work.run(schedulerCtx);
		}
	}
	
	public class SchedulerRunner implements Runnable {

		@Override
		public void run() {
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
		
	}
}

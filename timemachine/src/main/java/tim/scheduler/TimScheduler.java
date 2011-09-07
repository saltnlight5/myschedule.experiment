package tim.scheduler;

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
		store.addSchedule(schedule);
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
		while(started.get()) {
			for (Schedule schedule : store.getSchedules()) {
				String name = schedule.getName();
				for (Job job : schedule.getJobs()) {
					logger.info("Checking job {} with schedule {}.", job.getName(), name);
				}
			}
			sleep(500);
		}
	}
	
	@Override
	public void stopService() {
		logger.info("{} stopped.", this);
	}
	
	private void sleep(long millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			throw new RuntimeException("Failed to sleep full " + millisecs + " millisecs.", e);
		}
	}
}

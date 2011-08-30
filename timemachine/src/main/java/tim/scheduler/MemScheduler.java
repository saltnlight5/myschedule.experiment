package tim.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemScheduler implements Scheduler, Runnable {
	protected Logger logger = LoggerFactory.getLogger(getClass());			
	protected String name = "MemScheduler_" + UUID.randomUUID().toString();
	protected Map<String, Schedule> schedules = new HashMap<String, Schedule>();
	protected SchedulerConfig schedulerConfig = new DefaultSchedulerConfig();
	
	protected AtomicBoolean running = new AtomicBoolean(false) ;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void addSchedule(Schedule schedule) {
		String name = schedule.getName();
		schedules.put(name, schedule);
		logger.info("New schedule {} added to scheduler with jobs={}", name, schedule.getJobs());
	}
	
	@Override
	public String toString() {
		return "MemScheduler[" + name + "]";
	}
	
	@Override
	public void start() {
		Executor executor = schedulerConfig.getExecutor();
		executor.execute(this);
		logger.info("{} started.", this);
	}
	@Override
	public void stop() {
		running = new AtomicBoolean(false) ;
		logger.info("{} stopped.", this);
	}
	@Override
	public void init() {
		logger.info("{} initialized.", this);
	}
	@Override
	public void destroy() {
		logger.info("{} destroyed.", this);
	}
	
	/**
	 * Start running this scheduler. 
	 * 
	 * <p>Client should not call this method. It should be invoked inside start() method by an Executor.
	 */
	@Override
	public void run() {
		running = new AtomicBoolean(true) ;
		while(running.get()) {
			for (Map.Entry<String, Schedule> entry : schedules.entrySet()) {
				String name = entry.getKey();
				Schedule schedule = entry.getValue();
				for (Job job : schedule.getJobs()) {
					logger.info("Checking job {} with schedule {}.", job.getName(), name);
				}
			}
			sleep(500);
		}
	}
	
	private void sleep(long millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			throw new RuntimeException("Failed to sleep full " + millisecs + " millisecs.", e);
		}
	}
}

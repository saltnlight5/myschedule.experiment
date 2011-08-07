package deng.quartz.jboss.examples;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A service to start and stop Quartz scheduler */
public class QuartzSchedulerService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected String configName;
	protected Scheduler scheduler;
	protected boolean autoStart = true;
	protected boolean waitForJobsToComplete = true;
	
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}
	public void setWaitForJobsToComplete(boolean waitForJobsToComplete) {
		this.waitForJobsToComplete = waitForJobsToComplete;
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void start() {
		StdSchedulerFactory factory = null;
		try {
			if (configName != null) {
				logger.debug("Creating scheduler with config: {}", configName);
				factory = new StdSchedulerFactory(configName);
			} else {
				logger.debug("Creating scheduler with default 'quartz.properties' in classpath");
				factory = new StdSchedulerFactory();
			}
			scheduler = factory.getScheduler();
			logger.info("Quartz scheduler {} created.", scheduler.getSchedulerName());
			
			if (autoStart) {
				scheduler.start();
				logger.info("Quartz scheduler {} started.", scheduler.getSchedulerName());
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void stop() {
		if (scheduler != null) {
			try {
				String name = scheduler.getSchedulerName();
				logger.debug("Shutingdown Quartz scheduler {} with waitForJobsToComplete={}", name, waitForJobsToComplete);
				scheduler.shutdown(waitForJobsToComplete);
				logger.debug("Quartz scheduler {} has been shutdown successfully.", name);
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

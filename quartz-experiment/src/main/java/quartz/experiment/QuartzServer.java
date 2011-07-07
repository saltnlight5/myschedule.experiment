package quartz.experiment;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * A simple main program to start a Quartz Server. User may use -Dconfig=quartz.properties
 * to customize the scheduler.
 *
 * @author Zemian Deng
 */
public class QuartzServer {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected String config;
	
	protected Scheduler scheduler;
	
	public QuartzServer() {
		config = System.getProperty("config");
	}
	
	public void run() {
		if (scheduler == null)
			init();
		registerShutdown();
		try {
			boolean cont = beforeSchedulerStart();
			if (cont) {
				logger.info("Starting scheduler.");
				scheduler.start();
				afterSchedulerStart();
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void registerShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override public void run() {
				logger.info("Shutting down scheduler.");
				try {
					scheduler.shutdown(true);
				} catch (SchedulerException e) {
					throw new RuntimeException(e);
				}
				logger.info("Scheduler is done.");
			}
		});
	}

	protected void init() {
		logger.info("Initializing scheduler with config=" + config);
		try {
			if (config != null) {
				scheduler = new StdSchedulerFactory(config).getScheduler(); 
			} else {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		QuartzServer main = new QuartzServer();
		main.run();
	}

	// Support methods
	/** Placeholder method for subclass to add functionality.
	 * @return true to continue start the scheduler, or false to NOT start scheduler. 
	 */
	protected boolean beforeSchedulerStart() {
		return true;
	}

	/** Placeholder method for subclass to add functionality. */
	protected void afterSchedulerStart() {
	}

	protected void createSimpleJob(String name, long startDelay, long interval, int repeatCount, Class<? extends Job> jobClass, Map<String, Object> data) {
		// Note: repeatCount of -1 means repeat forever.
		if (repeatCount > 0)
			repeatCount -= 1; // Auto adjust to total number of repeating count b/c Quartz is repeating count + 1 times!
		Date startTime = new Date(System.currentTimeMillis() + startDelay);
		JobDetail job = newJob(jobClass).withIdentity(name).build();
		if (data != null)
			job.getJobDataMap().putAll(data);
		Trigger trigger = newTrigger().withIdentity(name).startAt(startTime).
				withSchedule(simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(repeatCount)).build();
		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}

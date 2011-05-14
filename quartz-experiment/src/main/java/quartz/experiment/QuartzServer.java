package quartz.experiment;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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
	
	private static Logger logger = LoggerFactory.getLogger(QuartzServer.class);
	
	private String config;
	
	private Scheduler scheduler;
	
	public void run() {
		if (scheduler == null)
			init();
		registerShutdown();
		try {
			logger.info("Starting scheduler.");
			scheduler.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void registerShutdown() {
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

	private void init() {
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
		main.config = System.getProperty("config");
		main.run();
	}

}

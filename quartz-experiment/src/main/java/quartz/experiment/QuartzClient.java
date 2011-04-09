package quartz.experiment;

import java.io.FileReader;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.plugins.xml.XMLSchedulingDataProcessorPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * A simple main program to start a Quartz Client. User may use -Dconfig=quartz.properties
 * to customize the scheduler.
 * 
 * <p>
 * User must ensure 'org.quartz.scheduler.rmi.proxy = true' configuration property is
 * set, and will not call scheduler start() nor shutdown() operation.
 * 
 * <p>
 * This client will use XMLSchedulingDataProcessorPlugin to load a 'loadJobsFilename' file
 * upon running. Not that this plugin will not be executed even if you added into the quartz
 * configuration when you set proxy to true (client mode.).
 * 
 * @author Zemian Deng
 */
public class QuartzClient {
	
	private static Logger logger = LoggerFactory.getLogger(QuartzClient.class);
	
	private String config;
	
	private String loadJobsFilename;
	
	private Scheduler scheduler;
	
	public void run() {
		if (scheduler == null)
			init();
		try {
			processClient();
			logger.info("Client is done.");
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	protected void processClient() throws SchedulerException {
		logger.info("Initializing and load XMLSchedulingDataProcessorPlugin explicitly.");
		XMLSchedulingDataProcessorPlugin processor = new XMLSchedulingDataProcessorPlugin();
		processor.setFileNames(loadJobsFilename);
		processor.setScanInterval(0);
		processor.initialize(null, scheduler);
		processor.start();
		processor.shutdown();
	}

	private void init() {
		logger.info("Initializing scheduler with config=" + config);
		try {
			if (config == null) {
				throw new RuntimeException("Missing quartz configuration file.");
			}
			Properties props = new Properties();
			props.load(new FileReader(config));
			if (!props.containsKey("org.quartz.scheduler.rmi.proxy")) {
				throw new RuntimeException("You must enable org.quartz.scheduler.rmi.proxy for Quartz Client mode.");
			}
			scheduler = new StdSchedulerFactory(config).getScheduler();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		QuartzClient main = new QuartzClient();
		main.config = System.getProperty("config");
		main.loadJobsFilename = System.getProperty("loadJobsFilename");
		main.run();
	}

}

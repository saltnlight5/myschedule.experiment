package quartz.experiment.classloader;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClassLoaderDebuggerPlugin implements SchedulerPlugin {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected String pluginName;
	
	@Override
	public void initialize(String name, Scheduler scheduler) throws SchedulerException {
		this.pluginName = name;
		
		// Current thread info
		logger.info("Current Thread: " + Thread.currentThread());
		logger.info("Current Thread ContextClassLoader: " + Thread.currentThread().getContextClassLoader());
		
		// Scheduler class loader info.
		logger.info("Scheduler class: " + scheduler.getClass());
		logger.info("Scheduler class location: " + scheduler.getClass().getProtectionDomain().getCodeSource().getLocation());
		logger.info("Scheduler class loader: " + scheduler.getClass().getClassLoader());
		
		// Try to test Quartz's ClassLoaderHelper
		CascadingClassLoadHelperExt clhelper = new CascadingClassLoadHelperExt();
		clhelper.initialize();
		String jobClassName = "quartz.experiment.classloader.SimpleJob";
		try {
			logger.info("Before loading job class.");
			logger.info("CascadingClassLoadHelperExt getBestCandidate " + clhelper.getBestCandidate());
			logger.info("CascadingClassLoadHelperExt getClassLoader " + clhelper.getClassLoader());
			
			Class<?> jobClass = clhelper.loadClass(jobClassName);
			logger.info("After loading job class.");
			logger.info("CascadingClassLoadHelperExt Found job class: " + jobClass);
			logger.info("CascadingClassLoadHelperExt Found job class classloader: " + jobClass.getClassLoader());
			logger.info("CascadingClassLoadHelperExt Found job class location: " + jobClass.getProtectionDomain().getCodeSource().getLocation());
			logger.info("CascadingClassLoadHelperExt getBestCandidate " + clhelper.getBestCandidate());
			logger.info("CascadingClassLoadHelperExt getClassLoader " + clhelper.getClassLoader());
		} catch (ClassNotFoundException e) {
			logger.error("Failed to find class: " + jobClassName, e);
//			throw new RuntimeException(e);
		}
		
		// StaticClassLoaderDebugger info
		StaticClassLoaderDebugger.showClassLoaderInfo();
		
		logger.info(this + " " + pluginName + " initialized.");
	}

	@Override
	public void start() {
//		logger.info(this + " " + pluginName + " started.");
	}

	@Override
	public void shutdown() {
//		logger.info(this + " " + pluginName + " shutdown.");
	}
}

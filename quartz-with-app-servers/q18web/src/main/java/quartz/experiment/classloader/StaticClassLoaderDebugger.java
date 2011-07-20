package quartz.experiment.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticClassLoaderDebugger {
	
	private static Logger logger = LoggerFactory.getLogger(StaticClassLoaderDebugger.class);

	static {
		logger.info("Start static initializer");
		showClassLoaderInfo();
		logger.info("End static initializer");
	}
	
	public static void showClassLoaderInfo() {
		Class<?> cls = StaticClassLoaderDebugger.class;
		logger.info(cls + " is loaded from url " + cls.getProtectionDomain().getCodeSource().getLocation());
		logger.info(cls + " classloader " + cls.getClassLoader());
	}

}

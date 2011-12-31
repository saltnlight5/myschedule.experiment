package deng.timemachine;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;


public class SchedulerConfig {
	
	public static final SchedulerConfig DEFAULT_CONFIG = loadPropsConfig(mkURL("classpath:timscheduler.properties"));
	protected String schedulerName;
	protected int workThreadPoolSize;
	protected Class<? extends Store> storeClass;
	protected boolean shutdownThreadPoolImmediatly;
	
	public static SchedulerConfig loadPropsConfig(URL configUrl) {
		Properties props = loadProps(configUrl);
		SchedulerConfig config = new SchedulerConfig();
		String prefix = "tim.scheduler.";
		
		config.schedulerName = props.getProperty(prefix + "schedulerName", UUID.randomUUID().toString());
		config.workThreadPoolSize = Integer.parseInt(props.getProperty(prefix + "workThreadPoolSize", "4"));
		config.storeClass = createClass(props.getProperty(prefix + "storeClass", MemStore.class.getName()));
		config.shutdownThreadPoolImmediatly = Boolean.parseBoolean(props.getProperty(prefix + "shutdownThreadPoolImmediatly", "false"));
				
		return config;
	}
	
	private static <T> Class<T> createClass(String className) {
		try {
			@SuppressWarnings("unchecked")
			Class<T> cls = (Class<T>)Class.forName(className);
			return cls;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static Properties loadProps(URL url) {
		Properties props = new Properties();
		InputStream inStream = null;
		try {
			inStream = url.openStream();
			props.load(inStream);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config " + url, e);
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				throw new RuntimeException("Failed to close input stream for config " + url, e);
			}
		}
		return props;
	}
	
	public static URL mkURL(String urlValue) {
		try {
			return new URL(null, urlValue, new ClasspathURLStreamHandler());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Failed to make URL " + urlValue, e);
		}
	}
}

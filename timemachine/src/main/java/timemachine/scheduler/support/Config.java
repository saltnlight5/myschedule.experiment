package timemachine.scheduler.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.scheduler.Scheduler;
import timemachine.scheduler.SchedulerException;
import timemachine.scheduler.service.FixedSizeThreadPool;
import timemachine.scheduler.service.SchedulerEngine;

public class Config {
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	public static final String SYS_PROPS_KEY = "timemachine.scheduler.configUrl";
	public static final String DEFAULT_CONFIG_FILE = "classpath:timemachine/scheduler/scheduler-default.properties";
	private String configUrl;
	private EasyMap configMap;
		
	public Config() {
		this(DEFAULT_CONFIG_FILE);
	}
	
	public Config(String configUrl) {
		this.configUrl = configUrl;
		
		// We always load default values first.
		this.configMap = new EasyMap(DEFAULT_CONFIG_FILE);

		// If System Properties key exists, merge and overwrite the defaults.
		configMap.addConfigBySysProps(SYS_PROPS_KEY);
		
		// If user given configUrl, merge and overwrite the defaults.
		if (configUrl != null)
			configMap.addConfig(configUrl);
		
		logger.info("{} loaded.", this);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[url=" + configUrl + ", size=" + configMap.size() + "]";
	}
	
	// Supporting methods.
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<?> cls) {
		try {
			Object obj = cls.newInstance();
			return (T)obj;
		} catch (InstantiationException e) {
			throw new SchedulerException("Failed to create new instance: From " + cls, e);
		} catch (IllegalAccessException e) {
			throw new SchedulerException("Failed to create new instance: From " + cls, e);
		}
	}

	// TimeMachine Scheduler config creater/getters.
	public DataStore createDataStore() {
		Class<DataStore> cls = configMap.getConfigClass("timemachine.scheduler.dataStore.class");
		return newInstance(cls);
	}
	public ThreadPool createServiceThreadPool() {
		Class<ThreadPool> cls = configMap.getConfigClass("timemachine.scheduler.serviceThreadPool.class");
		ThreadPool obj = newInstance(cls);
		if (obj instanceof FixedSizeThreadPool) {
			FixedSizeThreadPool setter = (FixedSizeThreadPool)obj;
			setter.setName("ServiceThreadPool");
			setter.setSize(configMap.getConfigInt("timemachine.scheduler.serviceThreadPool.size"));
		}
		return obj;
	}
	public ThreadPool createJobTaskThreadPool() {
		Class<ThreadPool> cls = configMap.getConfigClass("timemachine.scheduler.jobTaskThreadPool.class");
		ThreadPool obj = newInstance(cls);
		if (obj instanceof FixedSizeThreadPool) {
			FixedSizeThreadPool setter = (FixedSizeThreadPool)obj;
			setter.setName("JobTaskThreadPool");
			setter.setSize(configMap.getConfigInt("timemachine.scheduler.jobTaskThreadPool.size"));
		}
		return obj;
	}
	public Scheduler createScheduler() {
		Class<Scheduler> cls = configMap.getConfigClass("timemachine.scheduler.schedulerEngine.class");
		Scheduler obj = newInstance(cls);
		if (obj instanceof SchedulerEngine) {
			SchedulerEngine setter = (SchedulerEngine)obj;
			Long id = configMap.getConfigLong("timemachine.scheduler.schedulerEngine.id", -1);
			if (id != -1)
				setter.setId(id);
			String name = configMap.getConfig("timemachine.scheduler.schedulerEngine.name", null);
			if (name != null)
				setter.setName(name);
			setter.setBatchSize(configMap.getConfigInt("timemachine.scheduler.schedulerEngine.scheduleChecker.batchSize"));
			setter.setBatchTimeWindow(configMap.getConfigLong("timemachine.scheduler.schedulerEngine.scheduleChecker.batchTimeWindow"));
			setter.setScheduleCheckerMaxInterval(configMap.getConfigLong("timemachine.scheduler.schedulerEngine.scheduleChecker.maxInterval"));
			setter.setScheduleCheckerMinInterval(configMap.getConfigLong("timemachine.scheduler.schedulerEngine.scheduleChecker.minInterval"));
			setter.setServiceThreadPool(createServiceThreadPool());
			setter.setJobTaskThreadPool(createJobTaskThreadPool());
			setter.setDataStore(createDataStore());
		}
		return obj;
	}
}

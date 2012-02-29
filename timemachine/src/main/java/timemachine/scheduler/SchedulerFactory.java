package timemachine.scheduler;

import timemachine.scheduler.support.Config;


public class SchedulerFactory {
	
	public static Scheduler createScheduler() {
		return createScheduler(new Config());
	}
	
	public static Scheduler createScheduler(String configFile) {
		return createScheduler(new Config(configFile));
	}
	
	public static Scheduler createScheduler(Config config) {
		Scheduler scheduler = config.createScheduler();
		return scheduler;
	}
}

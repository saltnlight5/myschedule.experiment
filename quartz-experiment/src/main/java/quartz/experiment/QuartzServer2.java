package quartz.experiment;

import static quartz.experiment.Utils.createMap;

public class QuartzServer2 extends QuartzServer {
	public static void main(String[] args) {
		new QuartzServer2().run();
	}
	
	@Override
	protected boolean beforeSchedulerStart() {
		logger.info("Adding 3 sleepy jobs");
		createJob("job1", 5000, 20000, 10000);
		createJob("job2", 10000, 20000, 10000);
		createJob("job3", 15000, 20000, 10000);
		
		return true; // start scheduler.
	}
	
	protected void createJob(String name, long delay, long interval, long sleepTime) {
		int repeatCount = -1; 
		createSimpleJob(name, delay, interval, repeatCount, SleepJob.class, createMap("sleepTime", sleepTime));
	}
}

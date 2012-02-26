package integration.timemachine;

import org.junit.Test;

import timemachine.Config;
import timemachine.FixedIntervalSchedule;
import timemachine.Job;
import timemachine.ScheduleUnit;
import timemachine.Scheduler;
import timemachine.TimeMachineScheduler;
import timemachine.jobtasks.LoggerJobTask;

public class TimeSchedulerSchedulerTest {
	
	@Test
	public void testScheduleSecondlyJob() throws Exception {
		Scheduler scheduler = new TimeMachineScheduler(new Config());
		scheduler.start();
		
		// Schedule a job and let scheduler run for 10 secs.
		Job job = new Job();
		job.setTaskClass(LoggerJobTask.class);
		job.addSchedule(new FixedIntervalSchedule(2, ScheduleUnit.SECONDS));
		scheduler.schedule(job);
		Thread.sleep(10000L);
		
		scheduler.destroy();
	}
}

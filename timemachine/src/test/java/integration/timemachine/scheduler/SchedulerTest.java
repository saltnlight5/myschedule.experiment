package integration.timemachine.scheduler;

import static timemachine.scheduler.Schedules.*;

import org.junit.Test;

import timemachine.scheduler.Job;
import timemachine.scheduler.Scheduler;
import timemachine.scheduler.SchedulerFactory;
import timemachine.scheduler.jobtask.LoggerJobTask;

public class SchedulerTest {
	
	@Test
	public void testScheduleSecondlyJob() throws Exception {
		Scheduler scheduler = SchedulerFactory.createScheduler();
		scheduler.start();
		
		// Schedule a job and let scheduler run for 10 secs.
		Job job = new Job();
		job.setTaskClass(LoggerJobTask.class);
		job.addSchedule(repeatSchedule(2, 3500, now()));
		scheduler.scheduleJob(job);
		Thread.sleep(10000L);
		
		scheduler.destroy();
	}
}

package timemachine.impl;

import org.junit.Test;

import timemachine.Job;
import timemachine.Schedule;
import timemachine.Scheduler;

public class MemorySchedulerTest {
	
	@Test
	public void testSchedulerStartStop() throws Exception {
		Scheduler scheduler = new MemoryScheduler();
		scheduler.init();
		scheduler.start();
		scheduler.stop();
		scheduler.destroy();
		
		// should be same as above.
		scheduler.start();
		scheduler.destroy();
		
		// we should able to start stop and then start again
		scheduler.start();
		scheduler.stop();
		scheduler.start();
		scheduler.destroy();
	}
	
	@Test
	public void testScheduleJob() {
		Scheduler scheduler = new MemoryScheduler();
		scheduler.init();
		
		Job job = new Job();
		job.setTaskClass(LoggerJobTask.class);
		Schedule schedule = new Schedule();
		scheduler.schedule(job, schedule);
	}
}

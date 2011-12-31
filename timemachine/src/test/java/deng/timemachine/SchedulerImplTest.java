package deng.timemachine;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deng.timemachine.Job;
import deng.timemachine.LoggerTask;
import deng.timemachine.OnceSchedule;
import deng.timemachine.Scheduler;
import deng.timemachine.SchedulerImpl;

public class SchedulerImplTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testDefaultName() {
		Scheduler sche = new SchedulerImpl();
		sche.init();
		logger.info("Default scheduler name is {}", sche.getName());
		Assert.assertThat(sche.getName(), Matchers.notNullValue());
	}
	
	@Test
	public void testOnceSchedule() throws Exception {
		Scheduler sche = new SchedulerImpl();
		try {
			OnceSchedule schedule = new OnceSchedule();
			sche.init();
			sche.addSchedule(schedule);
		} finally {
			sche.destroy();
		}
	}
	
	@Test
	public void testStart() throws Exception {
		Scheduler sche = new SchedulerImpl();
		try {
			Job job = new Job();
			job.setName("job1");
			job.setWorkClass(LoggerTask.class);
			
			OnceSchedule schedule = new OnceSchedule();
			schedule.addJob(job);
			
			sche.init();
			sche.addSchedule(schedule);
			
			sche.start();

			Thread.sleep(3000);
			sche.stop();
		} finally {
			sche.destroy();
		}
	}
}

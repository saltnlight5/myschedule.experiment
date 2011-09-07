package tim.scheduler;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tim.scheduler.TimScheduler;
import tim.scheduler.OnceSchedule;
import tim.scheduler.RunnableJob;
import tim.scheduler.Scheduler;
import tim.scheduler.SimpleRunnableJob;

public class TimSchedulerTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testDefaultName() {
		Scheduler sche = new TimScheduler();
		sche.init();
		logger.info("Default scheduler name is {}", sche.getName());
		Assert.assertThat(sche.getName(), Matchers.notNullValue());
	}
	
	@Test
	public void testOnceSchedule() throws Exception {
		Scheduler sche = new TimScheduler();
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
		Scheduler sche = new TimScheduler();
		try {
			RunnableJob job = new RunnableJob();
			job.setName("job1");
			job.setRunabbleClass(SimpleRunnableJob.class);
			
			OnceSchedule schedule = new OnceSchedule();
			schedule.addJob(job);
			
			sche.init();
			sche.addSchedule(schedule);
			
			sche.start();

//			Thread.sleep(3000);
//			sche.stop();
		} finally {
			sche.destroy();
		}
	}
}

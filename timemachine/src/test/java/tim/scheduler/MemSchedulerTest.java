package tim.scheduler;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tim.scheduler.MemScheduler;
import tim.scheduler.OnceSchedule;
import tim.scheduler.RunnableJob;
import tim.scheduler.Scheduler;
import tim.scheduler.SimpleRunnableJob;

public class MemSchedulerTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testDefaultName() {
		Scheduler sche = new MemScheduler();
		logger.info("Default scheduler name is {}", sche.getName());
		Assert.assertThat(sche.getName().startsWith("MemScheduler"), Matchers.is(true));
	}
	
	@Test
	public void testOnceSchedule() throws Exception {
		Scheduler sche = new MemScheduler();
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
		Scheduler sche = new MemScheduler();
		try {
			RunnableJob job = new RunnableJob();
			job.setName("job1");
			job.setRunabbleClass(SimpleRunnableJob.class);
			
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

package quartz.experiment;

import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerWithJobDetailTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = TriggerWithJobDetailTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testTriggerWithJobDetail() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		
		prepJobs(scheduler);
		
		scheduler.start();
		Thread.sleep(9000);
		scheduler.shutdown();
	}

	private void prepJobs(Scheduler scheduler) throws Exception {
		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(JOB_NAME).build();
		Trigger trigger = TriggerBuilder.newTrigger().
				withIdentity(JOB_NAME).
				withSchedule(SimpleScheduleBuilder.
						simpleSchedule().withRepeatCount(1).withIntervalInMilliseconds(3000)
				)./*forJob(jobDetail).*/
				build();
		scheduler.scheduleJob(jobDetail, trigger);
	}
}

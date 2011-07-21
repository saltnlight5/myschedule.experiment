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

public class QuickTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = QuickTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testSimpleJob() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		// Schedule a simple job runs every 3 secs forever.
		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(JOB_NAME).build();
		Trigger trigger = TriggerBuilder.newTrigger().
				withIdentity(JOB_NAME).
				withSchedule(
						SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(3000)
				).build();
		scheduler.scheduleJob(jobDetail, trigger);
		
		scheduler.start();
		Thread.sleep(9000);
		scheduler.shutdown();
	}
}

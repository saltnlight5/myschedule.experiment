package quartz.experiment;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.*;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTest {
	private static final Logger logger = LoggerFactory.getLogger(QuickTest.class);

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = QuickTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testQuartz() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		JobDetail jobDetail = newJob(SimpleJob.class)
				.withIdentity(JOB_NAME, JOB_GROUP)
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(
						simpleSchedule()
						.repeatForever()
						.withIntervalInMilliseconds(3000))
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		logger.info("Job {} scheduled.", jobDetail.getKey());

		logger.info("Start scheduler and let it run for 60 secs.");
		scheduler.start();
		Thread.sleep(60 * 1000L);
		scheduler.shutdown();
		logger.info("Scheduler is done.");
	}
}

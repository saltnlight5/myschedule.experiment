package quartz.experiment;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecoveraleJobTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.database";
	public static final String JOB_NAME = RecoveraleJobTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testScheduleRecoverableJob() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		JobDetail jobDetail = newJob(SleepJob.class)
				.withIdentity(JOB_NAME)
				.usingJobData("sleepTime", 15 * 60 * 1000L)
				.requestRecovery()
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(
						simpleSchedule())
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		
		scheduler.start();
		Thread.sleep(Long.MAX_VALUE);
		scheduler.shutdown();
	}
	
	@Test
	public void testScheduleNonRecoverableJob() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		JobDetail jobDetail = newJob(SleepJob.class)
				.withIdentity(JOB_NAME + "_NonRecoverable")
				.usingJobData("sleepTime", 15 * 60 * 1000L)
				.requestRecovery(false)
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(
						simpleSchedule())
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		
		scheduler.start();
		Thread.sleep(Long.MAX_VALUE);
		scheduler.shutdown();
	}
	
	@Test
	public void testRunScheduler() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.start();
		Thread.sleep(Long.MAX_VALUE);
		scheduler.shutdown();
	}
}

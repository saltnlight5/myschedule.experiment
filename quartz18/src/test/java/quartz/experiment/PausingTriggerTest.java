package quartz.experiment;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Paused trigger will survive a scheduler restart when using database!
 */
public class PausingTriggerTest
{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG = "quartz/experiment/quartz.properties.database_oracle";
	public static final String JOB_NAME = "testPausingSimpleJob";
	public static final String JOB_GROUP = "DEFAULT";
	
	@Test
	public void testScheduleJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		// If trigger already exist, unschedule it.
		Trigger trigger = scheduler.getTrigger(JOB_NAME, JOB_GROUP);
		if (trigger != null) {
			scheduler.unscheduleJob(JOB_NAME, JOB_GROUP);
			logger.info("Job unscheduled.");
		}

		JobDetail jobDetail = new JobDetail(JOB_NAME, SimpleJob.class);
		trigger = new CronTrigger(JOB_NAME, JOB_GROUP, "0/3 * * * * ?");
				
		scheduler.scheduleJob(jobDetail, trigger);
		logger.info("Job scheduled.");
		
		scheduler.shutdown();
	}
	
	@Test
	public void testRunJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.start();
		Thread.sleep(9000);
		scheduler.shutdown();
	}
	
	@Test
	public void testPauseTrigger() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.start();
		Thread.sleep(9000);
		
		scheduler.pauseTrigger(JOB_NAME, JOB_GROUP);
		Thread.sleep(9000);
		
		scheduler.shutdown();
	}
	
	@Test
	public void testResumeTrigger() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.start();
		Thread.sleep(9000);
				
		scheduler.resumeTrigger(JOB_NAME, JOB_GROUP);
		Thread.sleep(9000);
		
		scheduler.shutdown();
	}
	
	@Test
	public void testUnscheduleJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.unscheduleJob(JOB_NAME, JOB_GROUP);
		scheduler.shutdown();
	}
}

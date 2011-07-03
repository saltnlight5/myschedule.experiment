package quartz.experiment;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbJobRunTest
{	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG = "quartz/experiment/quartz.properties.database";
	public static final String JOB_NAME = "testSimpleJob";
	public static final String JOB_GROUP = "DEFAULT";
	
	@Test
	public void testScheduleJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		
		String cronExpression = "0/3 * * * * ?";
		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(JOB_NAME).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(JOB_NAME).
				withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
		scheduler.scheduleJob(jobDetail, trigger);
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
	public void testUnscheduleJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.unscheduleJob(new TriggerKey(JOB_NAME));
		scheduler.shutdown();
	}
}

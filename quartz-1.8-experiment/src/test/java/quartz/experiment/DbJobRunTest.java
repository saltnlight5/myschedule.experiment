package quartz.experiment;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbJobRunTest
{	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG = "quartz/experiment/quartz.properties.database_oracle";
	public static final String JOB_NAME = "testSimpleJob";
	public static final String JOB_GROUP = "DEFAULT";
	
	@Test
	public void testScheduleJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		
		JobDetail jobDetail = new JobDetail(JOB_NAME, SimpleJob.class);
		Trigger trigger = new CronTrigger(JOB_NAME, JOB_GROUP, "0/3 * * * * ?");
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
		scheduler.unscheduleJob(JOB_NAME, JOB_GROUP);
		scheduler.shutdown();
	}
}

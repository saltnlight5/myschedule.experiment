package quartz.experiment;

import org.junit.Test;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.StatefulJob;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatefulJobTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	//public static final String CONFIG = "quartz/experiment/quartz.properties.database";
	public static final String JOB_NAME = StatefulJobTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testPersistentJobData() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		
		prepJobs(scheduler);
		
		scheduler.start();
		Thread.sleep(3000);
		scheduler.shutdown();
	}

	private void prepJobs(Scheduler scheduler) throws Exception {
		JobDetail jobDetail = JobBuilder.
				newJob(TestJob.class).
				withIdentity(JOB_NAME).
				usingJobData("count", 1).
				build();
		Trigger trigger = TriggerBuilder.newTrigger().
				withIdentity(JOB_NAME).
				withSchedule(SimpleScheduleBuilder.
						simpleSchedule().
						withRepeatCount(1).
						withIntervalInMilliseconds(1000)
				).
				build();
		scheduler.scheduleJob(jobDetail, trigger);
	}
	
	@DisallowConcurrentExecution
	@PersistJobDataAfterExecution
	public static class TestJob implements Job {
		protected Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobDataMap data = context.getJobDetail().getJobDataMap();
			logger.info("data map id " + data.hashCode());
			
			int count = context.getJobDetail().getJobDataMap().getInt("count");
			logger.info("Job is running... count = " + count);
			context.getJobDetail().getJobDataMap().put("count", count + 1);
		}
		
	}
	
	/*
	public static class TestJob2 implements StatefulJob {
		protected Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			int count = context.getJobDetail().getJobDataMap().getInt("count");
			logger.info("Job is running... count = " + count);
			context.getJobDetail().getJobDataMap().put("count", count + 1);
		}
		
	}
	*/	 
}

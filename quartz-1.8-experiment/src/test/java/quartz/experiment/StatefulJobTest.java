package quartz.experiment;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.StatefulJob;
import org.quartz.Trigger;
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
		JobDetail jobDetail = new JobDetail(JOB_NAME, "DEFAULT", TestJob2.class);
		jobDetail.getJobDataMap().put("count", 1);
		Trigger trigger = new SimpleTrigger(JOB_NAME, "DEFALT", 1, 1000);
		scheduler.scheduleJob(jobDetail, trigger);
	}
		
	public static class TestJob2 implements StatefulJob {
		protected Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			int count = context.getJobDetail().getJobDataMap().getInt("count");
			logger.info("Job is running... count = " + count);
			context.getJobDetail().getJobDataMap().put("count", count + 1);
		}
		
	}
	 
}

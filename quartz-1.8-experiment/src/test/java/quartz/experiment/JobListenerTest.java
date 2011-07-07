package quartz.experiment;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Note: Unless you use global job listener, the plain job listener requires you to add it to the scheduler, and
 * to each JobDetail!
 *
 * @author Zemian Deng
 */
public class JobListenerTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = JobListenerTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";
	public static final String JOB_LISTENER_NAME = JobListenerTest.class.getSimpleName();

	@Test
	public void testJobListener() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
	
		TestJobListener jobListener = new TestJobListener(JOB_LISTENER_NAME);
		scheduler.addJobListener(jobListener);
		
		prepJobs(scheduler);
		
		scheduler.start();
		Thread.sleep(3000);
		scheduler.shutdown();
	}

	private void prepJobs(Scheduler scheduler) throws Exception {
		JobDetail jobDetail = new JobDetail(JOB_NAME, "DEFAULT", SimpleJob.class);
		jobDetail.addJobListener(JOB_LISTENER_NAME); // Without this line, job listener will not be called!		
		jobDetail.getJobDataMap().put("count", 1);
		Trigger trigger = new SimpleTrigger(JOB_NAME, "DEFALT", 1, 1000);
		scheduler.scheduleJob(jobDetail, trigger);
	}
	
	public static class TestJobListener implements JobListener {
		
		protected Logger logger = LoggerFactory.getLogger(getClass());
		protected String name;
		
		public TestJobListener(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public void jobToBeExecuted(JobExecutionContext context) {
			logger.info("TestJobListener " + name + " jobToBeExecuted.");
		}

		@Override
		public void jobExecutionVetoed(JobExecutionContext context) {
			logger.info("TestJobListener " + name + " jobExecutionVetoed.");
		}

		@Override
		public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
			logger.info("TestJobListener " + name + " jobWasExecuted.");
		}
		
	}
}

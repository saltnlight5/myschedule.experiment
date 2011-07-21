package quartz.experiment;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = QuickTest.class.getSimpleName();
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
		JobDetail jobDetail = new JobDetail(JOB_NAME, "DEFAULT", SimpleJob.class);
		jobDetail.getJobDataMap().put("count", 1);
		Trigger trigger = new SimpleTrigger(JOB_NAME, "DEFALT", 1, 1000);
		scheduler.scheduleJob(jobDetail, trigger);
	}	 
}

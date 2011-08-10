package quartz.experiment;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When checking database, ensure you are looking at the right scheduler instance
 * name! Quartz 2.x allow multiple scheduler instance to be store in a single schema now.
 */
public class DbJobRunTest {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static final String CONFIG = "quartz/experiment/quartz.properties.database";
	public static final String JOB_NAME = DbJobRunTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testScheduleJob() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		String cronExpression = "0/3 * * * * ?";
		JobDetail jobDetail = newJob(SimpleJob.class)
				.withIdentity(JOB_NAME)
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(cronSchedule(cronExpression))
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.shutdown();
	}

	@Test
	public void testRunJob() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.start();
		Thread.sleep(9000);
		scheduler.shutdown();
	}

	@Test
	public void testUnscheduleJob() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		scheduler.unscheduleJob(new TriggerKey(JOB_NAME));
		scheduler.shutdown();
	}
	
	@Test
	public void testRemoveAllJobs() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		for (String group : scheduler.getJobGroupNames()) {
			@SuppressWarnings("unchecked")
			GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(group);
			for (JobKey jobKey : scheduler.getJobKeys(matcher)) {
				scheduler.deleteJob(jobKey);
				logger.info("Deleted job {}", jobKey);
			}
		}
		scheduler.shutdown();
	}
	
	@Test
	public void testListAllJobs() throws Exception {
		logger.info("Config {}", CONFIG);
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		for (String group : scheduler.getJobGroupNames()) {
			logger.info("Group {}", group);
			@SuppressWarnings("unchecked")
			GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(group);
			for (JobKey jobKey : scheduler.getJobKeys(matcher)) {
				logger.info("Job {}", jobKey);
			}
		}
		scheduler.shutdown();
	}
}

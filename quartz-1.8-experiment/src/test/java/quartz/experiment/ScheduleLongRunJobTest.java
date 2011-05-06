package quartz.experiment;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quartz.experiment.spring.QuartzClientBean;

/** 
 * Schedule jobs without starting scheduler
 *
 * @author Zemian Deng
 */
public class ScheduleLongRunJobTest {
	
	private static Logger logger = LoggerFactory.getLogger(ScheduleLongRunJobTest.class);

	@Test
	public void testAddOnetimeJobNow() throws Exception {
		String config = "quartz.properties.database";
		Scheduler scheduler = new StdSchedulerFactory(config).getScheduler();
		String randStr = UUID.randomUUID().toString().substring(0, 3);
		String jobName = "test_job_" + randStr;
		logger.info("Adding new onetime job " + jobName);
		JobDetail job = new JobDetail(jobName, LongRunJob.class);
		SimpleTrigger trigger = new SimpleTrigger(jobName);
		scheduler.scheduleJob(job, trigger);
	}
	
	@Test
	public void testAddOnetimeJobWithDelay() throws Exception {
		String config = "quartz.properties.database";
		Scheduler scheduler = new StdSchedulerFactory(config).getScheduler();
		String randStr = UUID.randomUUID().toString().substring(0, 3);
		String jobName = "test_job_" + randStr;
		long delayPeriod = 5 * 1000L;
		Date startTime = new Date(System.currentTimeMillis() + delayPeriod);
		logger.info("Adding new onetime job " + jobName);
		JobDetail job = new JobDetail(jobName, LongRunJob.class);
		SimpleTrigger trigger = new SimpleTrigger(jobName, startTime);
		scheduler.scheduleJob(job, trigger);
	}
	
	@Test
	public void testAddDailyJobNow() throws Exception {
		String config = "quartz.properties.database";
		Scheduler scheduler = new StdSchedulerFactory(config).getScheduler();
		String randStr = UUID.randomUUID().toString().substring(0, 3);
		String jobName = "test_job_" + randStr;
		logger.info("Adding new daily job " + jobName);
		JobDetail job = new JobDetail(jobName, LongRunJob.class);
		SimpleTrigger trigger = new SimpleTrigger(jobName, SimpleTrigger.REPEAT_INDEFINITELY, TriggerUtils.MILLISECONDS_IN_DAY);
		scheduler.scheduleJob(job, trigger);
	}
	
	@Test
	public void testDeleteJob() throws Exception {
		String config = "quartz.properties.database";
		Scheduler scheduler = new StdSchedulerFactory(config).getScheduler();
		String jobName = "test_job_141";
		logger.info("Deleting job " + jobName);
		scheduler.unscheduleJob(jobName, Scheduler.DEFAULT_GROUP);
		logger.info("Done.");
	}
	
	@Test
	public void testShowJobs() throws Exception {
		String config = "quartz.properties.database";
		Scheduler scheduler = new StdSchedulerFactory(config).getScheduler();
		QuartzClientBean service = new QuartzClientBean();
		service.setScheduler(scheduler);
		List<String[]> triggers = service.getTriggerNames();
		for (String[] jobName : triggers) {
			logger.info("job " + jobName[0] + "." + jobName[1]);
		}
		logger.info("There are " + triggers.size() + " jobs in scheduler.");
	}
}

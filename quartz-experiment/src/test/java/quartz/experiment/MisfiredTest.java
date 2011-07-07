package quartz.experiment;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MisfiredTest
 * 
 * @author Zemian Deng
 */
public class MisfiredTest {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testCreateDurableJob() throws Exception {
		JobDetail job = JobBuilder.newJob(SimpleJob.class).storeDurably().build();
		logger.info("job " + job);
	}

	@Test
	public void testRemoveAllJobs() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.database").getScheduler();
		List<String> groups = scheduler.getTriggerGroupNames();
		for (String group : groups) {
			@SuppressWarnings("unchecked")
			GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(group);
			Set<TriggerKey> keys = scheduler.getTriggerKeys(groupMatcher);
			for (TriggerKey key : keys) {
				Trigger trigger = scheduler.getTrigger(key);
				logger.info("Removing trigger: " + trigger);
				scheduler.unscheduleJob(key);
			}
		}
	}

	@Test
	public void testRunSchedulerWithJdbcJobStoreAndShutdownWithoutWait() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.database").getScheduler();

		scheduler.start();
		Thread.sleep(3000);
		scheduler.shutdown(false);
	}

	@Test
	public void testRunSchedulerWithJdbcJobStore() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.database").getScheduler();

		scheduler.start();
		Thread.sleep(10000);
		scheduler.shutdown(true);
	}

	@Test
	public void testCreateSleepJob() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.database").getScheduler();

		JobDetail sleepJob5000 = SleepJob.createJobDetail("sleepJob5000", 5000);
		Trigger sleepJob5000Trigger = newTrigger().withIdentity("sleepJob5000")
				.withSchedule(simpleSchedule().withRepeatCount(-1).withIntervalInSeconds(10)).build();

		scheduler.scheduleJob(sleepJob5000, sleepJob5000Trigger);
	}

	@Test
	public void testCreateSimpleJob() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.database").getScheduler();

		JobDetail simpleJob = SimpleJob.createJobDetail("simpleJob");
		Trigger simpleJobTrigger = newTrigger().withIdentity("simpleJob")
				.withSchedule(simpleSchedule().withRepeatCount(-1).withIntervalInSeconds(3)).build();

		scheduler.scheduleJob(simpleJob, simpleJobTrigger);
	}

	@Test
	public void testMisfiredWithJdbcJobStore() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.database").getScheduler();

		Date startTime = new Date(System.currentTimeMillis() + 3000);
		JobDetail longJob = SleepJob.createJobDetail("longJob", 5000);
		JobDetail misfiredJob = SimpleJob.createJobDetail("misfiredJob");
		Trigger longJobTrigger = newTrigger().withIdentity("longJobTrigger").withSchedule(simpleSchedule())
				.startAt(startTime).build();
		Trigger misfiredJobTrigger = newTrigger().withIdentity("misfiredJobTrigger")
				.withSchedule(simpleSchedule().withRepeatCount(-1).withIntervalInSeconds(1)).startAt(startTime).build();

		// Remove jobs first.
		scheduler.unscheduleJob(longJobTrigger.getKey());
		scheduler.unscheduleJob(misfiredJobTrigger.getKey());

		scheduler.scheduleJob(longJob, longJobTrigger);
		scheduler.scheduleJob(misfiredJob, misfiredJobTrigger);

		scheduler.start();
		Thread.sleep(5000); // allow the misfiredJobTrigger to have time to have misfired.
		scheduler.standby();

		// // Remove job after test.
		// scheduler.unscheduleJob(longJobTrigger.getKey());
		// scheduler.unscheduleJob(misfiredJobTrigger.getKey());

		scheduler.shutdown(true);
	}
	/** The "misfiredJob" is not show in log at all with RAMJobStore. */
	@Test
	public void testMisfiredWithRAMJobStore() throws Exception {
		Scheduler scheduler = new StdSchedulerFactory("quartz/experiment/quartz.properties.basic").getScheduler();

		Date startTime = new Date(System.currentTimeMillis() + 3000);
		JobDetail longJob = SleepJob.createJobDetail("longJob", 14000);
		JobDetail misfiredJob = SimpleJob.createJobDetail("misfiredJob");
		Trigger longJobTrigger = newTrigger().withIdentity("longJobTrigger").withSchedule(simpleSchedule())
				.startAt(startTime).build();
		Trigger misfiredJobTrigger = newTrigger().withIdentity("misfiredJobTrigger").withSchedule(simpleSchedule())
				.startAt(startTime).build();

		scheduler.scheduleJob(longJob, longJobTrigger);
		scheduler.scheduleJob(misfiredJob, misfiredJobTrigger);

		scheduler.start();
		Thread.sleep(15000);
		scheduler.shutdown(true);
	}
}

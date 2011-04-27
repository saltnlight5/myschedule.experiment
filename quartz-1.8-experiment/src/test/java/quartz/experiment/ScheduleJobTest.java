package quartz.experiment;

import java.util.Date;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This unit test will explore different ways to schedule job in Quartz(1.8.x). We will
 * not assert any values, but mostly will start a scheduler to run for few seconds
 * and observe the log output.
 *
 * @author Zemian Deng
 */
public class ScheduleJobTest {
	
	private static Logger logger = LoggerFactory.getLogger(ScheduleJobTest.class);

	@Test
	public void testScheduleOnetimeJobNow() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		SimpleTrigger trigger = new SimpleTrigger("job1");
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleOnetimeJobWithStartTime() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		Date startTime = new Date(System.currentTimeMillis() + 2000);
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		SimpleTrigger trigger = new SimpleTrigger("job1", startTime);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleOnetimeJobWithStartTimeInPast() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		// TIPS: If start time has past, it simply will invoke it immediately.
		Date startTime = new Date(System.currentTimeMillis() - 2000);
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		SimpleTrigger trigger = new SimpleTrigger("job1", startTime);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleRepeatJob() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		// TIPS: Quartz's repeatCount starts after the first run by trigger! So we need to subtract by 1
		//       if you need to get a total number of job execution count.
		int repeatCount = 2 - 1; // this will run job two times.
		int repeatInterval = 1000;
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		SimpleTrigger trigger = new SimpleTrigger("job1", repeatCount, repeatInterval);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
		
	@Test
	public void testScheduleRepeatJobWithStartTime() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		Date startTime = new Date(System.currentTimeMillis() + 2000);
		Date endTime = null;
		int repeatCount = 2 - 1;
		int repeatInterval = 1000;
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		SimpleTrigger trigger = new SimpleTrigger("job1", startTime, endTime, repeatCount, repeatInterval);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}

	@Test
	public void testScheduleRepeatJobForever() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		int repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
		int repeatInterval = 1000;
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		SimpleTrigger trigger = new SimpleTrigger("job1", repeatCount, repeatInterval);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}

	@Test
	public void testScheduleCronJob() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		// TIPS: You must give group name in CrontTrigger, or else you get this strange error:
		//       org.quartz.SchedulerException: Based on configured schedule, the given trigger will never fire.
		String cronExpr = "* * * * * ?"; // runs every secs.
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		CronTrigger trigger = new CronTrigger("job1", Scheduler.DEFAULT_GROUP, cronExpr);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleCronJobMemorialDay() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		String cronExpr = "0 0 0 ? 5 2L *"; // runs every year on last Monday of May at midnight
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		CronTrigger trigger = new CronTrigger("job1", Scheduler.DEFAULT_GROUP, cronExpr);
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		
		// Let's print next 10 fire times
		Date nextDate = new Date();
		int count = 0;
		while(count++ < 10) {
			Date fireTime = trigger.getFireTimeAfter(nextDate);
			logger.info("Next fireTime " + fireTime);
			nextDate = fireTime;
		}
		
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleCronJobThanksgivingDay() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		String cronExpr = "0 0 0 ? 11 5#4 *"; // runs every year on 4th Thursday of November at midnight
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		CronTrigger trigger = new CronTrigger("job1", Scheduler.DEFAULT_GROUP, cronExpr);
		
		scheduler.scheduleJob(job, trigger);
		
		// Let's print next 10 fire times
		Date nextDate = new Date();
		int count = 0;
		while(count++ < 10) {
			Date fireTime = trigger.getFireTimeAfter(nextDate);
			logger.info("Next fireTime " + fireTime);
			nextDate = fireTime;
		}
		
		scheduler.shutdown(true);
	}
}

package quartz.experiment;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForTotalCount;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/** 
 * This unit test will explore different ways to schedule job in Quartz(2.x). We will
 * not assert any values, but mostly will start a scheduler to run for few seconds
 * and observe the log output.
 *
 * @author Zemian Deng
 */
public class ScheduleJobTest {

	@Test
	public void testScheduleOnetimeJobNow() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();
	    Trigger trigger = newTrigger() 
	        .withIdentity("job1")
	        .withSchedule(simpleSchedule())
	        .build();
	    
	    scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleOnetimeJobWithStartTime() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		Date startTime = new Date(System.currentTimeMillis() + 2000);
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();    
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(simpleSchedule())
		    .startAt(startTime)
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleOnetimeJobWithStartTimeInPast() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		Date startTime = new Date(System.currentTimeMillis() - 2000);
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();    
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(simpleSchedule())
		    .startAt(startTime)
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleRepeatJob() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		int repeatCount = 2; // this will run job two times.
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(repeatSecondlyForTotalCount(repeatCount))
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleRepeatJob2() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		// TIPS: Quartz's repeatCount starts after the first run by trigger! So we need to subtract by 1
		//       if you need to get a total number of job execution count.
		int repeatCount = 2 - 1; // this will run job two times.
		int repeatInterval = 1000;
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(simpleSchedule()
		    		.withRepeatCount(repeatCount)
		    		.withIntervalInMilliseconds(repeatInterval))
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleRepeatJobWithStartTime() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		Date startTime = new Date(System.currentTimeMillis() + 2000);
		int repeatCount = 2 - 1; // this will run job two times.
		int repeatInterval = 1000;
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(simpleSchedule()
		    		.withRepeatCount(repeatCount)
		    		.withIntervalInMilliseconds(repeatInterval))
		    .startAt(startTime)
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleRepeatJobForever() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		int repeatInterval = 1000;
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();    
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(simpleSchedule()
		    		.repeatForever()
		    		.withIntervalInMilliseconds(repeatInterval))
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
	
	@Test
	public void testScheduleCronJob() throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		String cronExpr = "* * * * * ?"; // runs every secs.
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();    
		Trigger trigger = newTrigger() 
		    .withIdentity("job1")
		    .withSchedule(cronSchedule(cronExpr))
		    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		Thread.sleep(5000);
		scheduler.shutdown(true);
	}
}

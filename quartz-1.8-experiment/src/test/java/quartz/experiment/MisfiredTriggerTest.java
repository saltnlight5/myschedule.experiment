package quartz.experiment;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * You must ensure the org.quartz.jobStore.misfireThreshold is smaller than the trigger
 * fire interval in order to use the MISFIRE_INSTRUCTION_XXX policy properly! Otherwise
 * the trigger will be fired the number of time it was suppose to run (all at once.), 
 * despite it's scheduled time has passed.
 */
public class MisfiredTriggerTest
{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG = "quartz/experiment/quartz.properties.database_oracle_misfired";
	public static final String JOB_NAME = "testMisfiredSimpleJob";
	public static final String JOB_GROUP = "DEFAULT";
	
	@Test
	public void testScheduleJob() throws Exception
	{
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		// If trigger already exist, unschedule it.
		Trigger trigger = scheduler.getTrigger(JOB_NAME, JOB_GROUP);
		if (trigger != null) {
			scheduler.unscheduleJob(JOB_NAME, JOB_GROUP);
			logger.info("Job unscheduled.");
		}

		JobDetail jobDetail = new JobDetail(JOB_NAME, SimpleJob.class);

		// repeat only 3 times.
//		trigger = new SimpleTrigger(JOB_NAME, JOB_GROUP, 2, 2000);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
		
		// repeat forever.
		trigger = new SimpleTrigger(JOB_NAME, JOB_GROUP, -1, 2000);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY);
		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
//		trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
		
//		JobDetail jobDetail = new JobDetail(JOB_NAME, SimpleJob.class);
//		trigger = new CronTrigger(JOB_NAME, JOB_GROUP, "0/2 * * * * ?");
//		trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_SMART_POLICY);
//		trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
//		trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
		
		scheduler.scheduleJob(jobDetail, trigger);
		logger.info("Job scheduled. misfired=" + trigger.getMisfireInstruction() +  
				" repeatCount=" + ((SimpleTrigger)trigger).getRepeatCount() + 
				" repeatInterval=" + ((SimpleTrigger)trigger).getRepeatInterval());
		
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

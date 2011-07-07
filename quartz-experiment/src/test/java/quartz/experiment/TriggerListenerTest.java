package quartz.experiment;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerListenerTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = TriggerListenerTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testTriggerListener() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		
		prepSimpleJobs(scheduler);
		scheduler.getListenerManager().addTriggerListener(new TestTriggerListener());
		
		scheduler.start();
		Thread.sleep(9000);
		scheduler.shutdown();
	}

	private void prepSimpleJobs(Scheduler scheduler) throws Exception {
		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(JOB_NAME).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(JOB_NAME)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(1).withIntervalInMilliseconds(3000)).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}
	private void prepCrontJobs(Scheduler scheduler) throws Exception {		
		String cronExpression = "0/3 * * * * ?";
		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(JOB_NAME).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(JOB_NAME)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}
	
	public static class TestTriggerListener implements TriggerListener {
		
		protected Logger logger = LoggerFactory.getLogger(getClass());
		
		@Override
		public String getName() {
			return getClass().getName();
		}

		@Override
		public void triggerFired(Trigger trigger, JobExecutionContext context) {
			logger.info("triggerFired with " + trigger.getKey());
		}

		@Override
		public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
			logger.info("processing vetoJobExecution with " + trigger.getKey());
			// true to veto job.
			return true;
		}

		@Override
		public void triggerMisfired(Trigger trigger) {
			logger.info("triggerMisfired with " + trigger.getKey());
		}

		@Override
		public void triggerComplete(Trigger trigger, JobExecutionContext context,
				CompletedExecutionInstruction triggerInstructionCode) {
			logger.info("triggerComplete with " + trigger.getKey() + " and triggerInstructionCode=" + triggerInstructionCode);
		}
		
	}
}

package quartz.experiment;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.DailyCalendar;
import org.quartz.spi.MutableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = SchedulerTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testUpdateTrigger() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();
		
		// Schedule a simple job runs every 3 secs forever.
		JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(JOB_NAME).build();
		Trigger trigger = TriggerBuilder.newTrigger().
				withIdentity(JOB_NAME).
				withSchedule(
						SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(3000)
				).build();
		scheduler.scheduleJob(jobDetail, trigger);
		
		// Add a calendar that exclude work hours.
		scheduler.addCalendar("TESTCAL", new DailyCalendar("08:00", "17:00"), false, false);
		
		scheduler.start();
		Thread.sleep(4000); // first job should run now.
		
		// Let's verify stored trigger.
		MutableTrigger trigger2 = (MutableTrigger)scheduler.getTrigger(TriggerKey.triggerKey(JOB_NAME));
		assertThat(trigger2.getCalendarName(), nullValue());

		// Update trigger with calendar now.
		trigger2.setCalendarName("TESTCAL");
		scheduler.rescheduleJob(trigger2.getKey(), trigger2);
		Thread.sleep(4000); // second job may or may not run depending what hour you are in now.

		// Let's verify after update.
		Trigger trigger3 = scheduler.getTrigger(TriggerKey.triggerKey(JOB_NAME));
		assertThat(trigger3.getCalendarName(), is("TESTCAL"));
		
		scheduler.shutdown();
	}
}

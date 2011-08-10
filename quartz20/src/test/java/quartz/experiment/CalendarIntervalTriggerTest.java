package quartz.experiment;

import static org.quartz.JobBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarIntervalTriggerTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

//	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String CONFIG = "quartz/experiment/quartz.properties.database";
	public static final String JOB_NAME = CalendarIntervalTriggerTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testQuartz() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		JobDetail jobDetail = newJob(SimpleJob.class)
				.withIdentity(JOB_NAME)
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(
						calendarIntervalSchedule()
						.withIntervalInSeconds(35))
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		
		scheduler.start();
		Thread.sleep(60 * 1000L);
		scheduler.shutdown();
	}
}

package quartz.experiment;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.OperableTrigger;
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
	
	@Test
	public void testTrigger() throws Exception {		
		Date startTime = DateBuilder.dateOf(0, 0, 0, 1, 1, 2011);
		Trigger trigger = newTrigger()
				.withIdentity("test")
				.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
						.withIntervalInHours(1))
						.startAt(startTime)
						.build();
		List<Date> fireTimes = TriggerUtils.computeFireTimes((OperableTrigger)trigger, null, 48);
		for (Date fireTime : fireTimes)
			System.out.println(fireTime);
	}
}

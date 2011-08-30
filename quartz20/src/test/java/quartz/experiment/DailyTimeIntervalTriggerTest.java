package quartz.experiment;

import static org.quartz.DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule;
import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DailyTimeIntervalTriggerTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

//	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String CONFIG = "quartz/experiment/quartz.properties.database";
	public static final String JOB_NAME = DailyTimeIntervalTriggerTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testDailyTimeIntervalTrigger() throws Exception {		
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		JobDetail jobDetail = newJob(SimpleJob.class)
				.withIdentity(JOB_NAME)
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(dailyTimeIntervalSchedule()
						.withIntervalInMinutes(3)
						.startingDailyAt(new TimeOfDay(8, 0, 0))
						.endingDailyAt(new TimeOfDay(17, 0, 0))
						.withMisfireHandlingInstructionDoNothing())
				.startAt(dateOf(0, 0, 0))
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		
//		List<Date> fireTimes = TriggerUtils.computeFireTimes((OperableTrigger)trigger, null, 10);
//		int i=0; for (Date t : fireTimes) System.out.println("#"+(i++)+ " " + t);
		
		scheduler.start();
		Thread.sleep(60 * 1000L);
		scheduler.shutdown();
	}
}

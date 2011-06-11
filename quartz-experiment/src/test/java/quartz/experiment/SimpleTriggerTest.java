package quartz.experiment;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comment for SimpleTriggerTest.
 *
 * @author Zemian Deng
 */
public class SimpleTriggerTest
{
	private static Logger logger = LoggerFactory.getLogger(SimpleTriggerTest.class);
	
	@Test
	public void testEvery3Days() throws Exception {
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2011-07-05 18:30");			        
		Trigger trigger = newTrigger()
		 			.withIdentity("test")
		 			.startAt(date).withSchedule(calendarIntervalSchedule().withIntervalInDays(3)).build();
		
		logger.info("Trigger type " + trigger.getClass());		
		List<Date> dates = TriggerHelper.getNextFireTimes(trigger, 20);
		for (Date dt : dates)
			logger.info("NextFireTime " + dt);
	}
}

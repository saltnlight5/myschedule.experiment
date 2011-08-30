package quartz.experiment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.TriggerUtils;

/**
 * Comment for SimpleTriggerTest.
 * 
 * @author Zemian Deng
 */
public class CronTriggerTest {
	@Test
	public void testCronFireTimes() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(2004, Calendar.MARCH, 25, 0, 0, 0);
		Date startTime = cal.getTime();
		CronTrigger trigger = new CronTrigger();
		trigger.setCronExpression("0 0 9 L-15 * ?");
		trigger.setStartTime(startTime);		
		List<Date> fireTimes = TriggerUtils.computeFireTimes(trigger, null, 10);
		for (Date fireTime : fireTimes)
			System.out.println("Fire time: " + fireTime);
	}
}

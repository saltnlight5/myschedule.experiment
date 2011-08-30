package quartz.experiment;

import static quartz.experiment.TriggerHelper.cronTrigger;
import static quartz.experiment.TriggerHelper.showFireTimes;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Comment for SimpleTriggerTest.
 * 
 * @author Zemian Deng
 */
public class CronTriggerTest {
	@Test
	public void testCronWithCalendar() throws Exception {
		//showFireTimes(cronTrigger("test", "0 0 8-10 * * ?"), 30);
		showFireTimes(cronTrigger("test2", "0 0 8 L-1 * ?"), 30);
	}

	@Test
	public void testLastDayOffSet() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(2004, Calendar.MARCH, 25, 0, 0, 0);
		Date startTime = cal.getTime();
		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression("0 0 9 L-15 * ?");
		trigger.setStartTime(startTime);
		
		// Note that due to offset, the normal TriggerUtils.computeFireTimes will NOT work.
		// since the consecutive date will bring back to a day before fireTime, so it will 
		// make it look like it will not fire properly. You must compute fire time with 
		// your own nextFireTime + offset
//		List<Date> fireTimes = TriggerUtils.computeFireTimes(trigger, null, 10);
//		for (Date fireTime : fireTimes)
//			System.out.println("Fire time: " + fireTime);
		
		Date fireTime = startTime;
		for (int i = 0; i < 10; i++) {
			System.out.print("Fire time after " + fireTime);
			fireTime = trigger.getFireTimeAfter(fireTime);
			System.out.println(" is " + fireTime);
			// need to increment next fireTime with the offset value (15 days)
			fireTime = new Date(fireTime.getTime() + 15 * 24 * 60 * 60 * 1000L);
		}
		
	}
}

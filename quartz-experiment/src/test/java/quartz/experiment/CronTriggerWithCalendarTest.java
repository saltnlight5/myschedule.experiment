package quartz.experiment;

import static quartz.experiment.TriggerHelper.cronTrigger;
import static quartz.experiment.TriggerHelper.showFireTimes;

import org.junit.Test;

/**
 * Comment for SimpleTriggerTest.
 * 
 * @author Zemian Deng
 */
public class CronTriggerWithCalendarTest {
	@Test
	public void testCronWithCalendar() throws Exception {
		showFireTimes(cronTrigger("test", "0 0 8-10 * * ?"), 30);
	}

}

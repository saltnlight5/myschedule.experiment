package quartz.experiment;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** CronTest
 *
 * @author Zemian Deng
 */
public class CronTest {
	private Logger logger = LoggerFactory.getLogger(CronTest.class);
	
	/** W - "Nth" weekdays of the month */
	@Test
	public void testW_Character() throws Exception {
		// First weekdays of the month.
		assertThat(CronExpression.isValidExpression("0 0 12 1W * ?"), is(true));
		showFireTimes(TriggerHelper.getNextFireTimes(new CronExpression("0 0 12 1W * ?"), 20));
		
		// But you must prefix with a number, or else it's invalid.
		assertThat(CronExpression.isValidExpression("0 0 12 W * ?"), is(false));
	}
	
	@Test
	public void testFirstAndLastDayOfMonth() throws Exception {
		// first day only.
		CronExpression cron1 = new CronExpression("0 0 8 1 * ?");
		showFireTimes(TriggerHelper.getNextFireTimes(cron1, 20));
		// last day only.
		CronExpression cron2 = new CronExpression("0 0 8 L * ?");
		showFireTimes(TriggerHelper.getNextFireTimes(cron2, 20));
		
		// How do I combine it?
		// Solution#1: Create one JobDetail, then two Triggers above.		
	}
	
	@Test
	public void testEveryOtherWeek() throws Exception {		
		// This will not work.
		CronExpression cron = new CronExpression("0 0 8 ? * 6/2");
		showFireTimes(TriggerHelper.getNextFireTimes(cron, 20));
	}

	private void showFireTimes(List<Date> nextFireTimes) {
		for (Date date : nextFireTimes)
			logger.info("Next fire time: " + date);
	}

}

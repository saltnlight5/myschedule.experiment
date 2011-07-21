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
	
	@Test
	public void testW_Character() throws Exception {
		assertThat(CronExpression.isValidExpression("0 0 12 1W * ?"), is(true));
		assertThat(CronExpression.isValidExpression("0 0 12 W * ?"), is(false)); // Not a valid expression!
	}
	
	@Test
	public void testFirstAndLastDayOfMonth() throws Exception {
		CronExpression cron = new CronExpression("0 0 8 L * ?");
		showFireTimes(TriggerHelper.getNextFireTimes(cron, 20));
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

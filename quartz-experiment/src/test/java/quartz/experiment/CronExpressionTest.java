package quartz.experiment;

import java.util.Date;

import org.junit.Test;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * CronExpressionTest
 * 
 * See http://quartz-scheduler.org/docs/api/2.0.0/index.html
 *
 * @author Zemian Deng
 */
public class CronExpressionTest {

	private static Logger logger = LoggerFactory.getLogger(CronExpressionTest.class);
	
	@Test
	public void testNthTheWeekInAMonth() throws Exception {
		showCronFireTimes("0 0 0 ? 11 5#4 *");    // every 4th THU in Nov
		showCronFireTimes("0 0 0 ? Nov THU#4 *"); // same
	}

	@Test
	public void testIncrements() throws Exception {
		// format => START/INC
		// Note that the increment value does NOT wrap after the max limit in field!
		// What this mean is the START value should always be <= MAX - INC, else it will always
		// be the START value!
		showCronFireTimes("0/17 0 12 * * ?", 10);
		showCronFireTimes("17/17 0 12 * * ?", 10);
		showCronFireTimes("50/17 0 12 * * ?", 10); // it will always be 50th secs
		showCronFireTimes("0 0 12 1 7/6 ?"); // it always be in July
		showCronFireTimes("0 0 12 1 7/3 ?"); // it always be in July
	}
	
	private void showCronFireTimes(String cronExpr) throws Exception {
		showCronFireTimes(cronExpr, 10);
	}
	private void showCronFireTimes(String cronExpr, int count) throws Exception {
		showCronFireTimes(new CronExpression(cronExpr), new Date(), count);
	}
	
	private void showCronFireTimes(CronExpression cron, Date startTime, int count) {
		logger.info("cron=" + cron);
		Date nextDate = startTime;
		int i = 0;
		while(i++ < count) {
			Date fireTime = cron.getTimeAfter(nextDate);
			logger.info("Next fireTime " + fireTime);
			nextDate = fireTime;
		}
	}
}

package quartz.experiment.bugs;

import java.util.Date;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Qtz157
 *
 * @author Zemian Deng
 */
public class Qtz157 {
	
	private static Logger logger = LoggerFactory.getLogger(Qtz157.class);
	
	@Test
	public void testTriggerBuilder() throws Exception {
		long startTime = System.currentTimeMillis();
		CronTrigger trigger = new CronTrigger("t", "g", "* * * * * ?");
        trigger.setStartTime(new Date(startTime - 60000));
        trigger.setEndTime(new Date(startTime - 30000));
        logger.info("start time " + new Date(startTime));
		logger.info("next fire time " + trigger.getFireTimeAfter(new Date(startTime - 75000)));
		logger.info("next fire time " + trigger.getFireTimeAfter(new Date(startTime - 45000)));
		logger.info("next fire time " + trigger.getFireTimeAfter(new Date(startTime - 15000)));
	}
}

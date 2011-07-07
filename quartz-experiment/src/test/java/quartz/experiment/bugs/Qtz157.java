package quartz.experiment.bugs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Qtz157
 * 
 * @author Zemian Deng
 */
public class Qtz157 {

	private static Logger logger = LoggerFactory.getLogger(Qtz157.class);

	@Test
	public void testTriggerBuilder() throws Exception {

		long startTime = System.currentTimeMillis();

		CronTriggerImpl trigger1 = new CronTriggerImpl("t1", "g", "* * * * * ?");
		trigger1.setStartTime(new Date(startTime - 60000));
		trigger1.setEndTime(new Date(startTime - 30000));

		CronTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity("t2", "some trigger group")
				.startAt(new Date(startTime - 60000)).endAt(new Date(startTime - 30000))
				.withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?")).build();

		Date nextFireTime1 = trigger1.getFireTimeAfter(new Date(startTime - 45000));
		Date nextFireTime2 = trigger2.getFireTimeAfter(new Date(startTime - 45000));

		assertThat(nextFireTime1.getTime(), is(nextFireTime2.getTime()));
	}
}

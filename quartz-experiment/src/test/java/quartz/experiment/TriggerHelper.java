package quartz.experiment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerHelper {
	
	protected static Logger logger = LoggerFactory.getLogger(TriggerHelper.class);
	
	public static List<Date> getNextFireTimes(Trigger trigger, int maxCount) {
		List<Date> result = new ArrayList<Date>(maxCount);
		Date nextDate = new Date();
		int count = 0;
		while (count++ < maxCount) {
			nextDate = trigger.getFireTimeAfter(nextDate);
			result.add(nextDate);
		}
		return result;
	}
	
	public static void showFireTimes(Trigger trigger, int maxCount) {
		List<Date> dates = TriggerHelper.getNextFireTimes(trigger, 20);
		for (Date dt : dates)
			logger.info("Fire time: " + dt);
	}
	
	public static CronTrigger cronTrigger(String name, String cron) throws ParseException {
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name)
				.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		return trigger;
	}
}

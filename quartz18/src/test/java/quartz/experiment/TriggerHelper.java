 package quartz.experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;

public class TriggerHelper {
	
	public static List<Date> getNextFireTimes(CronExpression cron, int maxCount) {
		List<Date> result = new ArrayList<Date>(maxCount);
		Date nextDate = new Date();
		int count = 0;
		while(count++ < maxCount) {
			Date fireTime = cron.getNextValidTimeAfter(nextDate);
			result.add(nextDate);
			nextDate = fireTime;
		}
		return result;
	}
	
}

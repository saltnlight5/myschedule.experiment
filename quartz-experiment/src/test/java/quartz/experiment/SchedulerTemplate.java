package quartz.experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Scheduler;
import org.quartz.Trigger;

public class SchedulerTemplate {
	protected Scheduler scheduler;
	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public SchedulerTemplate(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public List<Date> getNextFireTimes(Trigger trigger, Date startTime, int maxCount) {	
		List<Date> list = new ArrayList<Date>();
		Date nextDate = startTime;
		int count = 0;
		while(count++ < maxCount) {
			Date fireTime = trigger.getFireTimeAfter(nextDate);
			list.add(fireTime);
			if (fireTime == null)
				break;
			nextDate = fireTime;
		}
		return list;
	}
}

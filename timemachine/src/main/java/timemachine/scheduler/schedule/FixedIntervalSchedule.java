package timemachine.scheduler.schedule;

import java.util.Date;

import timemachine.scheduler.Schedules;
import timemachine.scheduler.ScheduleUnit;
import timemachine.scheduler.SchedulerException;
import timemachine.scheduler.support.AbstractSchedule;


public class FixedIntervalSchedule extends AbstractSchedule {
	private Integer interval;
	private ScheduleUnit intervalUnit;
	private Long intervalInMillis;
	private Date endTime;
	private int endCount;
	private int currentCount;
	
	public FixedIntervalSchedule() {
		this(1, ScheduleUnit.MINUTE, new Date());
	}
	public FixedIntervalSchedule(int interval, ScheduleUnit unit, Date startTime) {
		if (startTime == null)
			startTime = new Date();
		
		if (unit == ScheduleUnit.MONTH || unit == ScheduleUnit.YEAR)
			throw new SchedulerException("Failed to create schedule: Unit " + unit + " is not fixed time unit.");
			
		this.intervalUnit = unit;
		this.interval = interval;
		this.desc = "{interval=" + interval + " " + intervalUnit + "}";
		this.startTime = startTime;
		this.nextRun = startTime;
	}
	
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public ScheduleUnit getIntervalUnit() {
		return intervalUnit;
	}
	public void setIntervalUnit(ScheduleUnit intervalUnit) {
		this.intervalUnit = intervalUnit;
	}
	public Long getIntervalInMillis() {
		if (intervalInMillis == null)
			intervalInMillis = covertToMillis(interval, intervalUnit);
		return intervalInMillis;
	}
	private Long covertToMillis(int interval, ScheduleUnit unit) {
		long result;
		if (unit == ScheduleUnit.SECOND)
			result = Schedules.MILLIS_IN_SECOND * interval;
		else if (unit == ScheduleUnit.MINUTE)
			result = Schedules.MILLIS_IN_MINUTE * interval;
		else if (unit == ScheduleUnit.HOUR)
			result = Schedules.MILLIS_IN_HOUR * interval;
		else if (unit == ScheduleUnit.DAY)
			result = Schedules.MILLIS_IN_DAY * interval;
		else if (unit == ScheduleUnit.WEEK)
			result = Schedules.MILLIS_IN_WEEK * interval;
		else
			throw new SchedulerException("Failed to create schedule: Invalid unit=" + unit);
			
		return result;
	}
	
	@Override
	public Date computeNextRun(Date after) {
		if (endCount > 0 && currentCount >= endCount) {
			return null;
		}
		
		long afterTimeMillis = incrementDate(after).getTime();
		long startTimeMillis = startTime.getTime();
		long unitMillis = getIntervalInMillis();
		long numOfUnitPassed = (afterTimeMillis - startTimeMillis) / unitMillis;
		//org.slf4j.LoggerFactory.getLogger(getClass()).info("afterTimeMillis=" + afterTimeMillis + ", startTimeMillis=" + startTimeMillis + ", unitMillis=" + unitMillis + ", numOfUnitPassed=" + numOfUnitPassed);
		
		long nextRunTimeMillis = startTimeMillis + (numOfUnitPassed * unitMillis);
		while(nextRunTimeMillis < afterTimeMillis)
			nextRunTimeMillis += unitMillis;
		Date result = new Date(nextRunTimeMillis);
		
		if (endTime != null && result.after(endTime)) {
			return null;
		} 
			
		return result;
	}

	@Override
	public void updateAfterRunByScheduler() {
		nextRun = computeNextRun(nextRun);
	}
	
	public void endTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public void endCount(int endCount) {
		this.endCount = endCount;
	}
}

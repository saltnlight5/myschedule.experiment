package timemachine;

import java.util.Date;


public class FixedIntervalSchedule extends AbstractSchedule {
	private Integer interval;
	private ScheduleUnit intervalUnit;
	private Long intervalInMillis;
	
	public FixedIntervalSchedule() {
		this(1, ScheduleUnit.MINUTES);
	}
	public FixedIntervalSchedule(int interval, ScheduleUnit unit) {
		this.intervalUnit = unit;
		if (unit == ScheduleUnit.MONTHS || unit == ScheduleUnit.YEARS)
			throw new SchedulerException("Failed to create schedule: Unit " + unit + " is not fixed time unit.");
		this.interval = interval;
		this.desc = "{interval=" + interval + " " + intervalUnit + "}";
		this.startTime = new Date();
		this.nextRun = computeNextRun(startTime);
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
		if (unit == ScheduleUnit.SECONDS)
			result = DateUtil.MILLIS_IN_SECOND;
		else if (unit == ScheduleUnit.MINUTES)
			result = DateUtil.MILLIS_IN_MINUTE;
		else if (unit == ScheduleUnit.HOURS)
			result = DateUtil.MILLIS_IN_HOUR;
		else if (unit == ScheduleUnit.DAYS)
			result = DateUtil.MILLIS_IN_DAY;
		else if (unit == ScheduleUnit.WEEKS)
			result = DateUtil.MILLIS_IN_WEEK;
		else
			throw new SchedulerException("Failed to create schedule: Invalid unit=" + unit);
			
		return result;
	}
	
	@Override
	public Date computeNextRun(Date after) {
		long afterTime = after.getTime() + DateUtil.MILLIS_IN_SECOND; // Increment to the smallest unit in scheudler.
		long startTimeMillis = startTime.getTime();
		long unitMillis = getIntervalInMillis();
		long numOfUnitPassed = (afterTime - startTimeMillis) / unitMillis;
		long nextRunTime = startTimeMillis + (numOfUnitPassed * unitMillis) + unitMillis;
		Date result = new Date(nextRunTime);
		return result;
	}
}

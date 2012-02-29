package timemachine.scheduler.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timemachine.scheduler.Job;
import timemachine.scheduler.Schedule;
import timemachine.scheduler.Schedules;


public abstract class AbstractSchedule extends AbstractData implements Schedule {
	protected Long id;
	protected String name;
	protected String desc;
	protected Date nextRun;
	protected Date startTime;
	protected List<Job> jobs = new ArrayList<Job>();
	
	@Override
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	public void addJob(Job job) {
		jobs.add(job);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getNextRun() {
		return nextRun;
	}
	
	public List<Date> computeNexRunDates(Date after, int maxCount) {
		List<Date> result = new ArrayList<Date>();
		Date nextRunDate = after;
		for (int i=0; i<maxCount; i++) {
			nextRunDate = computeNextRun(nextRunDate);
			result.add(nextRunDate);
		}
		return result;
	}
	
	@Override
	public int compareTo(Schedule other) {
		Date otherNextRun = other.getNextRun();
		if (nextRun != null && otherNextRun != null)
			return nextRun.compareTo(otherNextRun);
		else if (nextRun == null && otherNextRun != null)
			return -1;
		else if (nextRun != null && otherNextRun == null)
			return 1;
		else
			return 0;
	}

	/** Reset the smallest field and add by one unit to move forward the date time value for calculation. */
	protected Date incrementDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTime().getTime() +  + Schedules.MILLIS_IN_SECOND);
	}
}

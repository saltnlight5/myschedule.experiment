package timemachine;

import java.util.Date;


public abstract class AbstractSchedule implements Schedule, Comparable<Schedule> {
	protected Long id;
	protected String name;
	protected String desc;
	/** If null it means there is no more nextRun */
	protected Date nextRun;
	protected Date startTime;
	
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
	public Date getNextRun() {
		return nextRun;
	}
	public void setNextRun(Date date) {
		this.nextRun = date;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ", name=" + name + "]";
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
}

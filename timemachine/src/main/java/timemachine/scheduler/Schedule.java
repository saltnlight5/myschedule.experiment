package timemachine.scheduler;

import java.util.Date;
import java.util.List;

import timemachine.scheduler.support.Data;

public interface Schedule extends Data, Comparable<Schedule> {
	public Long getId();
	public void setId(Long id);
	public String getName();
	public String getDesc();
	public Date getNextRun();
	public Date getStartTime();
	public Date computeNextRun(Date after);
	public void updateAfterRunByScheduler();
	public List<Date> computeNexRunDates(Date after, int maxCount);
	public List<Job> getJobs();
	public void addJob(Job job);
}

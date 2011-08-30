package tim.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/** A schedule that runs job only one. */
public class OnceSchedule implements Schedule {
	protected Date startTime = new Date();
	protected boolean ended;
	protected List<Job> jobs = new ArrayList<Job>();
	protected String name = "OnceSchedule_" + UUID.randomUUID().toString();

	public void addJob(Job job) {
		jobs.add(job);
	}
	
	public String getName() {
		return name;
	}
	
	public List<Job> getJobs() {
		return Collections.unmodifiableList(jobs);
	}

	public boolean isEnded() {
		return ended;
	}
	
	public Date getNextRunTime() {
		return startTime;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}

package tim.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A schedule that runs job only one. */
public class OnceSchedule implements Schedule {
	
	private static final Logger logger = LoggerFactory.getLogger(OnceSchedule.class);
	protected Date startTime = new Date();
	protected AtomicBoolean ended = new AtomicBoolean(false);
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
		return ended.get();
	}
	
	public Date getNextRunTime() {
		if (!isEnded())
			return startTime;
		else
			return null;
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

	@Override
	public void init() {
		logger.debug("Initializing {}.", name);
	}

	@Override
	public void updateNextRunTime() {
		ended.set(true);
	}
}

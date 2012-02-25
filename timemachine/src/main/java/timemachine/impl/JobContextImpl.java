package timemachine.impl;

import timemachine.Job;
import timemachine.JobContext;
import timemachine.Schedule;
import timemachine.Scheduler;

public class JobContextImpl implements JobContext {
	private Scheduler scheduler;
	private Job job;
	private Schedule schedule;
	
	public JobContextImpl(Scheduler scheduler, Job job, Schedule schedule) {
		this.scheduler = scheduler;
		this.job = job;
		this.schedule = schedule;
	}

	@Override
	public Scheduler getScheduler() {
		return scheduler;
	}

	@Override
	public Job getJob() {
		return job;
	}

	@Override
	public Schedule getSchedule() {
		return schedule;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[job.id=" + job.getId() + 
			", schedule.id=" + schedule.getId() + 
			", scheduler.id=" + scheduler.getId() + "]";
	}
}

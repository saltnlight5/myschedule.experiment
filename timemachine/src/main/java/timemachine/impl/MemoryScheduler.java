package timemachine.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.Job;
import timemachine.Schedule;
import timemachine.Scheduler;
import timemachine.SchedulerException;

public class MemoryScheduler implements Scheduler {
	private static Logger logger = LoggerFactory.getLogger(MemoryScheduler.class);
	private Long id;
	private AtomicBoolean inited = new AtomicBoolean(false);
	private AtomicBoolean started = new AtomicBoolean(false);
	private MemoryIdGenerator idGenerator = new MemoryIdGenerator();
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void init() {
		if (id == null)
			id = idGenerator.generateId(MemoryScheduler.class.getName());
		logger.debug("Initializing {}", this);
		
		inited.set(true);
		logger.info("{} initialized.", this);
	}

	@Override
	public void destroy() {
		logger.debug("Destroying {}", this);
		if (started.get())
			stop();
		inited.set(false);
		logger.info("{} destroyed.", this);
	}

	@Override
	public void start() {
		logger.debug("Staring {}", this);
		if (!inited.get())
			init();
		started.set(true);
		logger.info("{} started.", this);
	}

	@Override
	public void stop() {
		logger.debug("Stopping {}", this);
		started.set(false);
		logger.info("{} stopped.", this);
	}
	
	@Override
	public String toString() {
		return "MemoryScheduler[id=" + id +"]";
	}

	@Override
	public boolean isInited() {
		return inited.get();
	}

	@Override
	public boolean isStarted() {
		return started.get();
	}

	@Override
	public void schedule(Job job, Schedule schedule) {
		if (!inited.get())
			throw new SchedulerException("Failed to schedule job: Scheduler has not be initialized yet.");
		if (job == null || schedule == null)
			throw new SchedulerException("Failed to schedule job: Job or schedule is not set.");
		
		if (job.getId() == null)
			job.setId(idGenerator.generateId(Job.class.getName()));
		if (schedule.getId() == null)
			schedule.setId(idGenerator.generateId(Job.class.getName()));
		logger.info("Scheduling {} with {}", job, schedule);
		
		if (job.getTaskClass() == null)
			throw new SchedulerException("Failed to schedule job: Job.taskClass is not set.");
		
		// TODO: how to schedule and run the job.
	}
}

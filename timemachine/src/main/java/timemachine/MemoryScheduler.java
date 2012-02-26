package timemachine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MemoryScheduler implements Scheduler {
	private static Logger logger = LoggerFactory.getLogger(MemoryScheduler.class);
	private Long id;
	private AtomicBoolean inited = new AtomicBoolean(false);
	private AtomicBoolean started = new AtomicBoolean(false);
	private MemoryIdGenerator idGenerator;
	private MemoryDataStore dataStore;
	private ExecutorService threadPool;
	private SchedulerRunner schedulerRunner;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public DataStore getDataStore() {
		return dataStore;
	}

	@Override
	public void init() {
		// Scheduler Id
		idGenerator = new MemoryIdGenerator();
		if (id == null)
			id = idGenerator.generateId(MemoryScheduler.class.getName());
		logger.debug("Initializing {}", this);
		
		// ThreadPool
		int maxJobTaskPoolSize = 2; 
		threadPool = Executors.newFixedThreadPool(maxJobTaskPoolSize + 1); // one extra for schedulerRunner
		
		// DataStore
		dataStore = new MemoryDataStore(idGenerator);

		// Scheduler Runner
		schedulerRunner = new SchedulerRunner(this);
		
		//Done
		inited.set(true);
		logger.info("{} initialized.", this);
	}
	
	@Override
	public void start() {
		logger.debug("Staring {}", this);
		if (!inited.get())
			init();
		
		threadPool.execute(schedulerRunner); // Add the scheduler runner into a thread pool to start jobs check loop.
		
		started.set(true);
		logger.info("{} started.", this);
	}

	@Override
	public void stop() {
		logger.debug("Stopping {}", this);
		started.set(false);
		
		schedulerRunner.stop(); // This will get this runner exit the run() and out of the thread pool.
		logger.info("{} stopped.", this);
	}

	@Override
	public void destroy() {
		logger.debug("Destroying {}", this);
		if (started.get())
			stop();
		
		threadPool.shutdown();

		inited.set(false);
		logger.info("{} destroyed.", this);
	}
	
	@Override
	public void pause() {
		if (!started.get())
			throw new SchedulerException("Failed to pause scheduler: It has not started yet.");
		schedulerRunner.pause();
		logger.info("{} paused.", this);
	}

	@Override
	public void resume() {
		if (!started.get())
			throw new SchedulerException("Failed to pause scheduler: It has not started yet.");
		schedulerRunner.resume();
		logger.info("{} resumed.", this);
	}

	@Override
	public boolean isPaused() {
		return schedulerRunner != null && schedulerRunner.isPaused();
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
		
		logger.debug("Scheduling {} with {}", job, schedule);
		if (job.getTaskClass() == null)
			throw new SchedulerException("Failed to schedule job: Job.taskClass is not set.");
		
		dataStore.storeData(job);
		dataStore.storeData(schedule);
		logger.info("{} scheduled. Next run: {}", job, schedule.getNextRun());
		
		// Update shedulerRunner with latest change
		schedulerRunner.wakeUpSleepCycle();
	}
}

package timemachine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.support.SchedulerRunner;


public class TimeMachineScheduler implements Scheduler {
	private static Logger logger = LoggerFactory.getLogger(TimeMachineScheduler.class);
	private Long id;
	private String name;
	private AtomicBoolean inited = new AtomicBoolean(false);
	private AtomicBoolean started = new AtomicBoolean(false);
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	private IdGenerator idGenerator;
	private DataStore dataStore;
	private ExecutorService threadPool;
	private SchedulerRunner schedulerRunner;
	private Config config;
	
	public TimeMachineScheduler() {
		this(new Config()); // default config
	}

	public TimeMachineScheduler(Config config) {
		this.config = config;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public DataStore getDataStore() {
		return dataStore;
	}

	@Override
	public void init() {
		if (destroyed.get())
			throw new SchedulerException("Failed to init scheduler: This instance has already marked as destroyed. Please use new instance.");
		
		if (config != null)
			initConfig();
		initServices();
		inited.set(true);
		logger.info("{} initialized.", this);
	}
	
	private void initConfig() {
		// Create scheduler services instances
		logger.debug("Initializing scheduler with {}.", config);
		
		// Id and name
		Class<IdGenerator> idGeneratorClass = config.getIdGeneatorClass();
		idGenerator = config.newInstance(idGeneratorClass);
		id = idGenerator.generateId(TimeMachineScheduler.class.getName());
		name = config.getSchedulerName();
		logger.debug("Initializing {}, name={}", this, name);
		
		// ThreadPool
		Class<ExecutorService> poolClass = config.getJobTaskThreadPoolClass();
		if (poolClass != null) {
			threadPool = config.newInstance(poolClass);
		} else {
			int maxJobTaskPoolSize = config.getJobTaskThreadPoolSize();
			threadPool = Executors.newFixedThreadPool(maxJobTaskPoolSize + 1); // one extra for schedulerRunner
		}
		
		// DataStore
		Class<DataStore> storeClass = config.getDataStoreClass();
		dataStore = config.newInstance(storeClass);
		if (dataStore instanceof MemoryDataStore) {
			MemoryDataStore memStore = (MemoryDataStore)dataStore;
			memStore.setIdGenerator(idGenerator);
		}
		
		// Scheduler Runner
		Class<SchedulerRunner> runnerClass = config.getSchedulerRunnerClass();
		schedulerRunner = config.newInstance(runnerClass);
		schedulerRunner.setScheduler(this);
		schedulerRunner.setJobThreadPool(threadPool);
		schedulerRunner.setBatchSize(config.getBatchSize());
		schedulerRunner.setBatchWindowsInMillis(config.getBatchWindowsInMillis());
		schedulerRunner.setMaxSchedulerCheckInterval(config.getMaxSchedulerCheckInterval());
		schedulerRunner.setMaxSchedulerCheckInterval(config.getMinSchedulerCheckInterval());
		schedulerRunner.setSchedulerCheckInterval(config.getMaxSchedulerCheckInterval());
	}
	
	private void initServices() {
		logger.debug("Initializing services for {}, name={}", this, name);
	}
	
	@Override
	public void start() {
		if (destroyed.get())
			throw new SchedulerException("Failed to init scheduler: This instance has already marked as destroyed. Please use new instance.");
		
		if (started.get())
			return;
		
		if (!inited.get())
			init();

		logger.debug("Starting {}", this);
		threadPool.execute(schedulerRunner); // Add the scheduler runner into a thread pool to start jobs check loop.
		
		started.set(true);
		logger.info("{} started.", this);
	}

	@Override
	public void stop() {
		if (!started.get())
			return;
		
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
		threadPool = null;

		inited.set(false);
		destroyed.set(true);
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
	public boolean isDestroyed() {
		return destroyed.get();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id +"]";
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
	public void schedule(Job job) {
		if (!inited.get())
			throw new SchedulerException("Failed to schedule job: Scheduler has not be initialized yet.");
		if (job == null)
			throw new SchedulerException("Failed to schedule job: Job or schedule is not set.");
		
		if (job.getTaskClass() == null)
			throw new SchedulerException("Failed to schedule job: Job.taskClass is not set.");

		dataStore.storeData(job);
		for (Schedule schedule : job.getSchedules()) {
			dataStore.storeData(schedule);
			logger.info("{} is with {}. The nextRun={}", new Object[]{ job, schedule, schedule.getNextRun() });
		}
		
		// Update shedulerRunner with latest change
		schedulerRunner.wakeUpSleepCycle();
	}

	@Override
	public void removeJob(Long jobId) {
		logger.debug("Removing job.id={}", jobId);
		dataStore.deleteData(jobId, Job.class);
	}
}
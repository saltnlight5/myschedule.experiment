package timemachine;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import timemachine.support.EasyMap;
import timemachine.support.SchedulerRunner;

public class Config extends EasyMap {
	private String id;
	private String fileName;
	
	public Config() {
		this(null);
	}
	
	public Config(String fileName) {
		super(fileName);
		this.fileName = fileName;
		this.id = UUID.randomUUID().toString();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ", fileName=" + fileName + "]";
	}
	
	public String getId() {
		return id;
	}
	public String getFileName() {
		return fileName;
	}

	public Class<IdGenerator> getIdGeneatorClass() {
		return getConfigClass("timemachine.scheduler.idGenerator.class", "timemachine.support.MemoryIdGenerator");
	}

	public String getSchedulerName() {
		return getConfig("timemachine.scheduler.name", "TimeMachineScheduler");
	}

	public int getJobTaskThreadPoolSize() {
		return getConfigInt("timemachine.scheduler.jobTaskThreadPoolSize", 4);
	}

	public int getBatchSize() {
		return getConfigInt("timemachine.scheduler.batchSize", 1);
	}

	public long getBatchWindowsInMillis() {
		return getConfigLong("timemachine.scheduler.batchWindowsInMillis", DateUtil.MILLIS_IN_SECOND);
	}

	public Class<ExecutorService> getJobTaskThreadPoolClass() {
		return getConfigClass("timemachine.scheduler.jobTaskThreadPool.class", null);
	}

	public Class<DataStore> getDataStoreClass() {
		return getConfigClass("timemachine.scheduler.jobTaskThreadPool.class", "timemachine.MemoryDataStore");
	}

	public Class<SchedulerRunner> getSchedulerRunnerClass() {
		return getConfigClass("timemachine.scheduler.schedulerRunner.class", "timemachine.support.SchedulerRunner");
	}

	public long getMaxSchedulerCheckInterval() {
		return getConfigLong("timemachine.scheduler.maxSchedulerCheckInterval", SchedulerRunner.MAX_SCHEDULER_CHECK_INTERVAL);
	}
	public long getMinSchedulerCheckInterval() {
		return getConfigLong("timemachine.scheduler.minSchedulerCheckInterval", SchedulerRunner.MIN_SCHEDULER_CHECK_INTERVAL);
	}
}

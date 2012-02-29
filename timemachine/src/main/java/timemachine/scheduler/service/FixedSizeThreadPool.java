package timemachine.scheduler.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timemachine.scheduler.support.AbstractService;
import timemachine.scheduler.support.ThreadPool;

public class FixedSizeThreadPool extends AbstractService implements ThreadPool {
	private int size = 8;
	private ExecutorService executor;
	
	public void setSize(int size) {
		this.size = size;
	}
		
	@Override
	protected void initService() {
		logger.debug("Creating a fixed thread pool, size={})", size);
		executor = Executors.newFixedThreadPool(size);
	}
	
	@Override
	public void destroyService() {
		logger.debug("Shutting down thread pool.");
		executor.shutdown();
		executor = null;
	}

	@Override
	public void execute(Runnable task) {
		executor.execute(task);
	}
}

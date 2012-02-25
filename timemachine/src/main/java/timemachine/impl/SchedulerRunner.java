package timemachine.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.DataStore;
import timemachine.Scheduler;

public class SchedulerRunner implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SchedulerRunner.class);
	private long schedulerCheckInterval = 15000L; // millis
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean paused = new AtomicBoolean(false);
	private Scheduler scheduler;
	private DataStore dataStore;
	
	public SchedulerRunner(Scheduler scheduler, DataStore dataStore) {
		this.scheduler = scheduler;
		this.dataStore = dataStore;
	}
	
	public void schedulerChanged() {
		// Await any blocking instances
		synchronized(this) {
			notifyAll(); // We typically will only have one runner, but just in case.
		}
	}
	
	public boolean isRunning() {
		return running.get();
	}
	
	public boolean isPaused() {
		return paused.get();
	}
	
	public void pause() {
		paused.set(true);
	}
	
	public void resume() {
		paused.set(false);
		schedulerChanged();
	}
	
	public void stop() {
		running.set(false);
		schedulerChanged();
	}
	
	@Override
	public void run() {
		// reset flags in case we re-run with the same instance.
		paused.set(false);
		running.set(true);
		
		// run scheduler check loop.
		while (running.get()) {
			if (!paused.get()) {
				checkScheduler();
			}
			sleepBetweenCheck();
		}
		logger.info("Scheduler runner is done.");
	}
	
	private void sleepBetweenCheck() {
		try {
			synchronized(this) {
				// Use wait on this object to block instead of Thread.sleep(), because we may need to notify/wake it
				// in case there is DataStore change notification.
				wait(schedulerCheckInterval);
			}
		} catch (InterruptedException e) {
			// If we can't sleep, we just continue anyway.
		}
	}

	protected void checkScheduler() {
		logger.debug("Checking scheduler for jobs to run.");
		// TODO: check jobs.
	}
}

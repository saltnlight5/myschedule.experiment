package org.quartz.extra;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionHistoryPlugin implements SchedulerPlugin, JobListener {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected String pluginName;
	
	@Override
	public void initialize(String name, Scheduler scheduler) throws SchedulerException {
		this.pluginName = name;
		scheduler.addGlobalJobListener(this);
		logger.info("ExecutionHistoryPlugin " + name + " initialized with itself as global listener.");
	}

	@Override
	public void start() {
		logger.info("ExecutionHistoryPlugin " + pluginName + " started.");
	}

	@Override
	public void shutdown() {
		logger.info("ExecutionHistoryPlugin " + pluginName + " shutdown.");
	}

	/** Return the JobListener name, same as plugin name. */
	@Override
	public String getName() {
		return pluginName;
	}
	
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		logger.info("ExecutionHistoryPlugin " + pluginName + " jobToBeExecuted.");
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("ExecutionHistoryPlugin " + pluginName + " jobExecutionVetoed.");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		logger.info("ExecutionHistoryPlugin " + pluginName + " jobWasExecuted.");
	}

}

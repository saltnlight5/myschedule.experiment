package timemachine.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.Job;
import timemachine.JobContext;
import timemachine.JobTask;
import timemachine.SchedulerException;

public class JobRunner implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(JobRunner.class);
	private JobContext jobContext;
	public JobRunner(JobContext jobContext) {
		this.jobContext = jobContext;
	}
	@Override
	public void run() {
		Job job = jobContext.getJob();
		Class<? extends JobTask> jobTaskClass = job.getTaskClass();
		logger.debug("Creating jobTask instance " + jobTaskClass.getName());
		try {
			JobTask task = jobTaskClass.newInstance();
			logger.info("Running jobTask: " + task);
			task.run(jobContext);
		} catch (InstantiationException e) {
			throw new SchedulerException("Failed to create JobTask: " + jobTaskClass, e);
		} catch (IllegalAccessException e) {
			throw new SchedulerException("Failed to create JobTask: " + jobTaskClass, e);
		}
	}
	
}
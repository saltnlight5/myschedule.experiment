package timemachine.jobtasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.JobContext;
import timemachine.JobTask;


public class LoggerJobTask implements JobTask {
	private static Logger logger = LoggerFactory.getLogger(LoggerJobTask.class);
	
	@Override
	public void run(JobContext jobContext) {
		logger.info("JobTask is running: {}", jobContext);
	}
}

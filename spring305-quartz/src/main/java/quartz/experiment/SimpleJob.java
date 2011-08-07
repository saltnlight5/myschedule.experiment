package quartz.experiment;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void execute(JobExecutionContext jobExecCtx) throws JobExecutionException {
		logger.info("Job is running: " + jobExecCtx.getJobDetail());
	}

}

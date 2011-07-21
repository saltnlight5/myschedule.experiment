package quartz.experiment;

import static org.quartz.JobBuilder.newJob;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** SimpleJob
 *
 * @author Zemian Deng
 */
public class SimpleJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJob.class);
	
	/**
	 * Override @see org.quartz.Job#execute(org.quartz.JobExecutionContext) method.
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job is running with context: " + context);
	}

	
	public static JobDetail createJobDetail(String name) {
		return newJob(SimpleJob.class)
	        .withIdentity(name)
	        .build();
	}
}

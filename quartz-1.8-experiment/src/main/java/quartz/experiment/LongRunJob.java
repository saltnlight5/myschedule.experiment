package quartz.experiment;

import java.io.File;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A job to simulate long running job.
 * 
 * This job will sleep until timeout period is reached, or stop immediately if it find 
 * a file name matching to the job name in the user home directory.
 *
 * @author Zemian Deng
 */
public class LongRunJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(LongRunJob.class);
	
	private long timeoutPeriod = 30 * 60 * 1000L; // 30 mins.
	
	private long fileCheckFrequency = 5 * 1000L; // 5 secs.
	
	private long jobStartedTime = new Date().getTime();

	/**
	 * Override @see org.quartz.Job#execute(org.quartz.JobExecutionContext) method.
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobName = context.getJobDetail().getName();
		String dir = System.getProperty("user.home");
		File stopFile = new File(dir + "/" + jobName);
		long elapseTime = 0;
		
		logger.info(jobName + " is starting: jobStartedTime=" + jobStartedTime + ", stopFile=" + stopFile);
		while (true) {
			elapseTime = System.currentTimeMillis() - jobStartedTime;
			if (elapseTime > timeoutPeriod)
				break;
			if (stopFile.exists()) {
				logger.info("Stop file found. Stopping job and removing " + stopFile);
				break;
			}
			sleep(fileCheckFrequency);
			logger.info(jobName + " is still running: elapseTime=" + elapseTime);
		}
		logger.info(jobName + " is done: elapseTime=" + elapseTime);
	}

	/**
	 * @param time
	 */
	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}

package quartz.experiment;

import static org.quartz.JobBuilder.newJob;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * SleepJob - simulate a long running job. Use the jobDataMap's "sleepTime" to set the time
 * in milliseconds to pause the job.
 *
 * @author Zemian Deng
 */
public class SleepJob implements Job {
	public static String SLEEP_TIME_PARAM = "sleepTime";
	protected Logger logger = LoggerFactory.getLogger(getClass());
		
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		String jobName = job.getKey().getName();
		long sleepTime = job.getJobDataMap().getLong(SLEEP_TIME_PARAM);
		logger.info(jobName + " is about to sleep for " + sleepTime + " ms.");
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new JobExecutionException("Failed to sleep " + sleepTime + " ms.", e);
		}
		logger.info(jobName + " is done.");
	}
	
	public static JobDetail createJobDetail(String name, long sleepTime) {
		return newJob(SleepJob.class)
	        .withIdentity(name)
	        .usingJobData(SLEEP_TIME_PARAM, sleepTime)
	        .build();
	}
}

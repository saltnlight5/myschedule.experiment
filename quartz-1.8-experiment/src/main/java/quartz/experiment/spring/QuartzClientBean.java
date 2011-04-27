package quartz.experiment.spring;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quartz.experiment.SimpleJob;

/** QuarzClientBean
 *
 * @author Zemian Deng
 */
public class QuartzClientBean {
	
	private static Logger logger = LoggerFactory.getLogger(QuartzClientBean.class);
	private Scheduler scheduler;
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void runAddJob() throws Exception {
		String cronExpr = "* * * * * ?"; // runs every secs.
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		CronTrigger trigger = new CronTrigger("job1", Scheduler.DEFAULT_GROUP, cronExpr);
		logger.info("Scheduling cron job: " + cronExpr);
		scheduler.scheduleJob(job, trigger);
		logger.info("Job has been added.");
	}
	
	public void runRemoveJob() throws Exception {
		String triggerName = "job1";
		logger.info("Removing job/trigger " + triggerName + " from scheduler.");
		boolean ret = scheduler.unscheduleJob(triggerName, Scheduler.DEFAULT_GROUP);
		if (ret)
			logger.info("Job/trigger has been removed");
		else
			logger.error("Failed to remove job/trigger.");
	}
}

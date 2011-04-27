package quartz.experiment.spring;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quartz.experiment.SimpleJob;

/** QuarzClientBean for quartz 2.0
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
		JobDetail job = newJob(SimpleJob.class)
	        .withIdentity("job1")
	        .build();
	    Trigger trigger = newTrigger() 
	        .withIdentity("job1")
	        .withSchedule(simpleSchedule())
	        .build();

		logger.info("Scheduling simple job: " + job);
		scheduler.scheduleJob(job, trigger);
		logger.info("Job has been added.");
	}
	
	public void runRemoveJob() throws Exception {
		String triggerName = "job1";
		logger.info("Removing job/trigger " + triggerName + " from scheduler.");
		boolean ret = scheduler.deleteJob(new JobKey("job1"));
		if (ret)
			logger.info("Job/trigger has been removed");
		else
			logger.error("Failed to remove job/trigger.");
	}
}

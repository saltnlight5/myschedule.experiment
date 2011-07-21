package quartz.experiment.spring;

import java.util.ArrayList;
import java.util.List;

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
	private String runMethodName = "showTriggerNames";
	
	public void setRunMethodName(String runMethodName) {
		this.runMethodName = runMethodName;
	}
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void run() {
		logger.info("Invoking " + runMethodName);
		try {
			if (runMethodName.equals("showTriggerNames"))
				showTriggerNames();
			else if (runMethodName.equals("addJob1"))
				addJob1();
			else if (runMethodName.equals("removeJob1"))
				removeJob1();
			else
				throw new Exception("Unkonwn method name " + runMethodName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void showTriggerNames() {
		List<String[]> triggers = getTriggerNames();
		if (triggers.size() ==0)
			logger.info("There is no triggers in scheduler yet.");
		else
			for (String[] pair : triggers)
				logger.info("Trigger: " + pair[1] + "." + pair[0]); //GROUP.TRIGGER_NAME
	}
	
	public void addJob1() throws Exception {
		String cronExpr = "* * * * * ?"; // runs every secs.
		JobDetail job = new JobDetail("job1", SimpleJob.class);
		CronTrigger trigger = new CronTrigger("job1", Scheduler.DEFAULT_GROUP, cronExpr);
		logger.info("Scheduling cron job: " + cronExpr);
		scheduler.scheduleJob(job, trigger);
		logger.info("Job has been added.");
	}
	
	public void removeJob1() throws Exception {
		String triggerName = "job1";
		logger.info("Removing job/trigger " + triggerName + " from scheduler.");
		boolean ret = scheduler.unscheduleJob(triggerName, Scheduler.DEFAULT_GROUP);
		if (ret)
			logger.info("Job/trigger has been removed");
		else
			logger.error("Failed to remove job/trigger.");
	}
	
	/**
	 * Get all trigger names in a list that contains group and name pair of array object.
	 */
	public List<String[]> getTriggerNames()
	{
		try {
			List<String[]> list = new ArrayList<String[]>();
			String[] triggerGroups = scheduler.getTriggerGroupNames();
			for (String triggerGroup : triggerGroups) {
				String[] triggerNames = scheduler.getTriggerNames(triggerGroup);
				for (String triggerName : triggerNames) {
					String[] pair = new String[2];
					pair[0] = triggerName;
					pair[1] = triggerGroup;
					list.add(pair);
				}
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

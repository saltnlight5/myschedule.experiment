package quartz.experiment;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * In order for Spring managed transaction to roll back any Quartz operations, you must add the
 * "rollbackFor" attribute of SchedulerException type! Or else your transaction will continue
 * to commit for scheduler, while other JPA/Hibernate tx are automatically managed properly.
 *
 * @author Zemian Deng
 */
@Transactional(rollbackFor={SchedulerException.class})
public class MySchedulerServiceImpl implements MySchedulerService {	
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Scheduler scheduler;
	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void scheduleJobs() throws Exception {
		logger.info("Scheduling job now.");
		
		JobDetail job1 = new JobDetail("job1", SimpleJob.class);
		job1.setDurability(true);
		scheduler.addJob(job1, false);
		
		JobDetail job2 = new JobDetail("job2", SimpleJob.class);
		//job2.setDurability(true);
		scheduler.addJob(job2, false); // this should throw exception because job is not durable.
	}

	@Override
	public void updateJobs() throws Exception {
	}

	@Override
	public void removeJobs() throws Exception {
		int triggerCount = 0;
		String[] groups = scheduler.getTriggerGroupNames();
		for (String group : groups) {
			String[] names = scheduler.getTriggerNames(group);
			for (String name : names) {
				boolean ret = scheduler.unscheduleJob(name, group);
				logger.info("Trigger unscheduled: {}. {} {}.", new Object[]{ret, name, group});
				triggerCount++;
			}
		}
		logger.info("{} triggers unscheduled.", triggerCount);
		
		int jobCount = 0;
		groups = scheduler.getJobGroupNames();
		for (String group : groups) {
			String[] names = scheduler.getJobNames(group);
			for (String name : names) {
				boolean ret = scheduler.deleteJob(name, group);
				logger.info("Orphaned Job deleted: {}. {} {}.", new Object[]{ret, name, group});
				jobCount++;
			}
		}
		logger.info("{} orphaned jobs deleted.", jobCount);
	}

	@Override
	public void showJobs() throws Exception {
		logger.debug("Querying scheduler info.");
		
		int jobCount = 0;
		String[] groups = scheduler.getJobGroupNames();
		for (String group : groups) {
			String[] names = scheduler.getJobNames(group);
			for (String name : names) {
				JobDetail job = scheduler.getJobDetail(name, group);
				logger.info("Job: {}", job);
				jobCount++;
			}
		}
		logger.info("{} jobs found.", jobCount);

		int triggerCount = 0;
		groups = scheduler.getTriggerGroupNames();
		for (String group : groups) {
			String[] names = scheduler.getTriggerNames(group);
			for (String name : names) {
				Trigger trigger = scheduler.getTrigger(name, group);
				logger.info("Trigger: {}", trigger);
				triggerCount++;
			}
		}
		logger.info("{} triggers found.", triggerCount);
	}
}

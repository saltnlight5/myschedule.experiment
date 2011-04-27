//package quartz.experiment;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.Map;
//
//import org.quartz.Job;
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.ScheduleBuilder;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SimpleScheduleBuilder;
//import org.quartz.SimpleTrigger;
//import org.quartz.TriggerBuilder;
//
//import static org.quartz.JobBuilder.*;
//import static org.quartz.TriggerBuilder.*;
//import static org.quartz.DateBuilder.*;
//import static org.quartz.SimpleScheduleBuilder.*;
//
//import springrunner.AbstractService;
//
//public class QuartzService extends AbstractService {
//
//	private Scheduler scheduler;
//	
//	/**
//	 * Setter
//	 * @param scheduler Scheduler, the scheduler to set
//	 */
//	public void setScheduler(Scheduler scheduler) {
//		this.scheduler = scheduler;
//	}
//
//	@Override
//	protected void startService() {
//		try {
//			scheduler.start();
//		} catch (SchedulerException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	protected void stopService() {
//		try {
//			scheduler.shutdown();
//		} catch (SchedulerException e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	public void scheduleOnetimeJob(String jobName, Class<? extends Job> job) {
//		scheduleOnetimeJob(jobName, job, null, null);
//	}
//
//	public void scheduleOnetimeJob(String jobName, Class<? extends Job> job, Date startTime) {
//		scheduleOnetimeJob(jobName, job, null, startTime);
//	}
//	public void scheduleOnetimeJob(String jobName, Class<? extends Job> job, JobDataMap jobData, Date startTime) {
//		JobBuilder jobBuilder = newJob(job).withIdentity(jobName);
//		if (jobData != null)
//			jobBuilder.usingJobData(jobData);
//		
//		TriggerBuilder<SimpleTrigger> triggerBuilder = newTrigger().withIdentity(jobName).withSchedule(simpleSchedule());
//		if (startTime != null)
//			triggerBuilder.startAt(startTime);
//		else
//			triggerBuilder.startNow();
//		
//		JobDetail jobDetail = jobBuilder.build();
//		SimpleTrigger jobTrigger = triggerBuilder.build();
//		
//		try {
//			scheduler.scheduleJob(jobDetail, jobTrigger);
//		} catch (SchedulerException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//}

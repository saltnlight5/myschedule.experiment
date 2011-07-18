package quartz.experiment;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class QuartzServer4 extends QuartzServer {
	public static void main(String[] args) {
		new QuartzServer4().run();
	}
	
	@Override
	protected boolean beforeSchedulerStart() {
		try {
			logger.info("Modifying and replacing job1 jobdetail and trigger.");
			JobDetail job = SleepJob.createJobDetail("job1", 3500);
			//scheduler.addJob(job, true);

			TriggerKey triggerKey = new TriggerKey("job1");			
			Trigger newTrigger = newTrigger().withIdentity(triggerKey).
					withSchedule(simpleSchedule().withIntervalInMilliseconds(3500).withRepeatCount(-1)).
					forJob("job1").build();	
			//scheduler.rescheduleJob(triggerKey, newTrigger);
			
			// Use a new way to replace job and trigger
			Map<JobDetail, List<Trigger>> triggersAndJobs = new HashMap<JobDetail, List<Trigger>>();
			List<Trigger> triggerList = new ArrayList<Trigger>();
			triggerList.add(newTrigger);
			triggersAndJobs.put(job, triggerList);
			scheduler.scheduleJobs(triggersAndJobs, true);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}		
		return true;
	}
}

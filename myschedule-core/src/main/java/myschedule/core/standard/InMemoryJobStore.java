package myschedule.core.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myschedule.core.Job;
import myschedule.core.JobSchedule;
import myschedule.core.JobStore;
import myschedule.core.Schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryJobStore implements JobStore {
	
	private static Logger logger = LoggerFactory.getLogger(InMemoryJobStore.class);

	/** A map of a jobName per JobSchedule in memory storage. */
	private Map<String, JobSchedule> jobScheduleStore = new HashMap<String, JobSchedule>(); 
	
	public void save(JobSchedule jobSchedule) {
		logger.debug("Saving " + jobSchedule);
		jobScheduleStore.put(jobSchedule.getJobName(), jobSchedule);
	}

	@Override
	public void init() {
		logger.debug("Init jobStore: " + this);
	}

	@Override
	public void destroy() {
		logger.debug("Destroying jobStore: " + this);
	}

	@Override
	public List<JobSchedule> getNextRunJobSchedules(long maxNextRunJobTime) {
		List<JobSchedule> jobSchedules = new ArrayList<JobSchedule>();
		//List<Schedule> schedules = findNexRunJobSchedules(maxNextRunJobTime);
		for (Map.Entry<String, JobSchedule> entry : jobScheduleStore.entrySet()) {
			//String jobName = entry.getKey();
			JobSchedule jobSchedule = entry.getValue();
			Schedule schedule = jobSchedule.getSchedule();
			long nextRunTime = System.currentTimeMillis() - schedule.getNextRunJobTime().getTime();
			if (nextRunTime <= maxNextRunJobTime) {
				jobSchedules.add(jobSchedule);
			}
		}
		return jobSchedules;
	}
}

package myschedule.core;

/**
 * 
 * @author Zemian Deng
 */
public class JobSchedule {
	private String jobName;
	private Job job;
	private Schedule schedule;
	
	private Class<? extends Job> jobClass;
	
	public JobSchedule(String jobName, Job job, Schedule schedule) {
		setJobName(jobName);
		setJob(job);
		setSchedule(schedule);
	}
	
	public JobSchedule() {
	}

	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public Class<? extends Job> getJobClass() {
		return jobClass;
	}
	public void setJobClass(Class<? extends Job> jobClass) {
		this.jobClass = jobClass;
	}
	
	@Override
	public String toString() {
		return "JobSchedule[jobName=" + jobName + ", shcedule=" + schedule + "]";
	}
}

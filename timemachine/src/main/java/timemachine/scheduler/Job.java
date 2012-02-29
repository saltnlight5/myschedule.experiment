package timemachine.scheduler;

import java.util.ArrayList;
import java.util.List;

import timemachine.scheduler.support.AbstractData;

public class Job extends AbstractData {
	private String name;
	private Class<? extends JobTask> taskClass;
	private List<Schedule> schedules = new ArrayList<Schedule>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<? extends JobTask> getTaskClass() {
		return taskClass;
	}
	public void setTaskClass(Class<? extends JobTask> taskClass) {
		this.taskClass = taskClass;
	}
	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
	public List<Schedule> getSchedules() {
		return schedules;
	}
	public void addSchedule(Schedule schedule) {
		schedules.add(schedule);
		schedule.addJob(this); //Ensure we tight both side association.
	}
}

package timemachine;

import java.util.ArrayList;
import java.util.List;

public class Job implements Data {
	private Long id;
	private String name;
	private Class<? extends JobTask> taskClass;
	private List<Schedule> schedules = new ArrayList<Schedule>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ", name=" + name + "]";
	}
}

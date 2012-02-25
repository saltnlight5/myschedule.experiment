package timemachine;

public class Job implements Data {
	private Long id;
	private String name;
	private Class<? extends JobTask> taskClass;
	
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
	
	@Override
	public String toString() {
		String taskClassStr = (taskClass != null) ? taskClass.getName() : "null";
		return getClass().getSimpleName() + "[id=" + id + ", name=" + name + ", taskClass=" + taskClassStr + "]";
	}
}

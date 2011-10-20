package tim.scheduler;

public class Job {
	
	protected Class<? extends Task> workClass;
	protected String name;
	
	public void setWorkClass(Class<? extends Task> workClass) {
		this.workClass = workClass;
	}
	
	public Class<? extends Task> getWorkClass() {
		return workClass;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Job[" + name + ", workClass=" + workClass.getName() + "]";
	}
}

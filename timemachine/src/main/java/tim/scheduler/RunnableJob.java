package tim.scheduler;

public class RunnableJob implements Job {
	
	protected Class<? extends Runnable> runabbleClass;
	protected String name;
	
	public void setRunabbleClass(Class<? extends Runnable> runabbleClass) {
		this.runabbleClass = runabbleClass;
	}
	public Class<? extends Runnable> getRunabbleClass() {
		return runabbleClass;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "RunnableJob[" + name + "]";
	}
}

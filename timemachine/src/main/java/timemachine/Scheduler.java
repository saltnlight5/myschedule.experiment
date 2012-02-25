package timemachine;

public interface Scheduler {
	public Long getId();
	public void init();
	public void destroy();
	public void start();
	public void stop();
	
	public boolean isInited();
	public boolean isStarted();
	
	public void schedule(Job job, Schedule schedule);
}

package timemachine;


public interface Scheduler {
	public Long getId();
	public String getName();
	
	public void init();
	public void start();
	public void stop();
	public void destroy();	
	public boolean isInited();
	public boolean isStarted();
	public boolean isDestroyed();
	
	public void pause();
	public void resume();
	public boolean isPaused();
	
	public DataStore getDataStore();
	public void schedule(Job job);
	public void removeJob(Long jobId);
}

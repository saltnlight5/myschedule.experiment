package tim.scheduler;

public interface Scheduler {
	String getName();
	
	void addSchedule(Schedule schedule);
	
	void start();
	
	void stop();
	
	void init();
	
	void destroy();
}

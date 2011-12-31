package deng.timemachine;

public interface Scheduler extends Service {
	
	String getName();
	void addSchedule(Schedule schedule);
	
}

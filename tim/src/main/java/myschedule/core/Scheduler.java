package myschedule.core;

/**
 * 
 * @author Zemian Deng
 */
public interface Scheduler extends InitDestroy, Service {
	String getName();
	void add(JobSchedule jobSchedule);
}

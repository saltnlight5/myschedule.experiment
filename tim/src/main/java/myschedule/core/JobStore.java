package myschedule.core;

import java.util.List;

/**
 * 
 * @author Zemian Deng
 */
public interface JobStore extends InitDestroy {
	void save(JobSchedule jobSchedule);

	List<JobSchedule> getNextRunJobSchedules(long maxNextRunJobTime);
}

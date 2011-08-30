package timemachine;

import java.util.Date;
import java.util.List;

public interface Schedule {
	void addJob(Job job);
	
	String getName();
	
	List<Job> getJobs();

	boolean isEnded();
	
	Date getNextRunTime();
	
	Date getStartTime();
}

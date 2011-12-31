package deng.timemachine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemStore implements Store {
	
	private static final Logger logger = LoggerFactory.getLogger(MemStore.class);
	protected Map<String, Schedule> schedulesMap = Collections.synchronizedMap(new HashMap<String, Schedule>());
	protected SchedulerConfig schedulerConfig;
	
	protected AtomicBoolean running = new AtomicBoolean(false) ;
		
	public void addSchedule(Schedule schedule) {
		logger.debug("Adding schedule {}.", schedule);
		String name = schedule.getName();
		schedulesMap.put(name, schedule);
		logger.info("New schedule {} added to scheduler with jobs={}", name, schedule.getJobs());
	}
	
	public Collection<Schedule> getSchedules() {
		return Collections.unmodifiableCollection(schedulesMap.values());
	}

	@Override
	public void removeSchedule(Schedule schedule) {
		String name = schedule.getName();
		schedulesMap.remove(name);
	}
}

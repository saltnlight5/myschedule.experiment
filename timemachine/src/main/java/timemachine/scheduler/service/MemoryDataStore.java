package timemachine.scheduler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.scheduler.Job;
import timemachine.scheduler.Schedule;
import timemachine.scheduler.SchedulerException;
import timemachine.scheduler.support.AbstractService;
import timemachine.scheduler.support.Data;
import timemachine.scheduler.support.DataStore;


public class MemoryDataStore extends AbstractService implements DataStore {
	private static Logger logger = LoggerFactory.getLogger(MemoryDataStore.class);
	/** Map of last used id value per data object's name (toString()). */
	private Map<String, Long> objectIds = new HashMap<String, Long>();
	/** Map of Job per id as key */
	private Map<Long, Job> jobs = Collections.synchronizedMap(new HashMap<Long, Job>());
	/** Map of Schedule per id as key */
	private Map<Long, Schedule> schedules = Collections.synchronizedMap(new HashMap<Long, Schedule>());
	/** List of sorted nextRun schedule ids. */
	private PriorityQueue<Schedule> schedulesQueue = new PriorityQueue<Schedule>();

	@Override
	public void storeData(Data data) {
		if (data instanceof Job) {
			Job job = (Job)data;
			if (job.getId() == null)
				job.setId(generateId(Job.class));
			jobs.put(job.getId(), job);
		} else if(data instanceof Schedule) {
			Schedule schedule = (Schedule)data;
			if (schedule.getId() == null) {
				schedule.setId(generateId(Schedule.class));
			}
			schedules.put(schedule.getId(), schedule);
			schedulesQueue.add(schedule);
		} else {
			throw new SchedulerException("Failed to store data: Invalid dataType=" + data.getClass().getName());
		}
		logger.info("New data stored: {}", data);
	}

	@Override
	public Data getData(Long id, Class<? extends Data> dataType) {
		Data data = null;
		if (Job.class.equals(dataType)) {
			data = jobs.get(id);
		} else if(Schedule.class.equals(dataType)) {
			data = schedules.get(id);
		} else {
			throw new SchedulerException("Failed to get data: Invalid dataType=" + dataType.getName());
		}
		logger.debug("Got data {}", data);
		return data;
	}

	@Override
	public List<Data> getAllData(Class<? extends Data> dataType) {
		List<Data> dataList = null;
		if (Job.class.equals(dataType)) {
			dataList = new ArrayList<Data>(jobs.values());
		} else if(Schedule.class.equals(dataType)) {
			dataList = new ArrayList<Data>(schedules.values());
		} else {
			throw new SchedulerException("Failed to get all data: Invalid dataType=" + dataType.getName());
		}
		logger.debug("Got dataType={} list size: {}", dataType, dataList.size());
		return dataList;
	}

	@Override
	public int getDataCount(Class<? extends Data> dataType) {
		if (Job.class.equals(dataType)) {
			return jobs.size();
		} else if(Schedule.class.equals(dataType)) {
			return schedules.size();
		} else {
			throw new SchedulerException("Failed to get data count: Invalid dataType=" + dataType.getName());
		}
	}

	@Override
	public Data deleteData(Long id, Class<? extends Data> dataType) {
		Data result = null;
		if (Job.class.equals(dataType)) {
			Job job = jobs.remove(id);
			for (Schedule schedule : job.getSchedules()) {
				if (schedule.getJobs().size() == 0)
					removeSchedule(schedule.getId());
			}
		} else if(Schedule.class.equals(dataType)) {
			result = removeSchedule(id);
		} else {
			throw new SchedulerException("Failed to delete data: Invalid dataType=" + dataType.getName());
		}
		if (result == null)
			throw new SchedulerException("Failed to delete data: Id " + id + " not found for dataType=" + dataType.getName());
		
		logger.info("Deleted dataType={} with id={}", dataType.getName(), id);
		return result;
	}
	
	private Schedule removeSchedule(Long id) {
		Schedule schedule = schedules.remove(id);
		schedulesQueue.remove(schedule);
		
		// Remove all job references
		for (Job job : schedule.getJobs()) {
			job.getSchedules().remove(schedule);
		}
		
		return schedule;
	}

	@Override
	public List<Schedule> getSchedulesToRun(int maxCount, Date cutoffTime) {
		List<Schedule> result = new ArrayList<Schedule>();
		while (result.size() < maxCount && schedulesQueue.size() > 0) {
			Schedule schedule = schedulesQueue.poll();
			Date nextRun = schedule.getNextRun();
			
			// If schedule has come to end, let's remove from store and continue.
			if (nextRun == null) {
				logger.info("{} has no more nextRun time, so remove from store.", schedule);
				schedules.remove(schedule.getId());
				continue;
			}
			
			// If schedule has reached cutoff time, then we are done.
			if (nextRun.after(cutoffTime)) {
				// Since this schedule time did not met our criteria, we must re-add back into the queue.
				schedulesQueue.add(schedule);
				break; // stop searching now.
			}
			
			logger.debug("Adding {} for next run", schedule);
			result.add(schedule);
		}
		
		// Ensure we update next run schedule
		for (Schedule schedule : result) {
			logger.debug("Scheduler is updating {} after job has submited.", schedule);
			Date prevRun = schedule.getNextRun();
			schedule.updateAfterRunByScheduler();
			Date nextRun = schedule.getNextRun();
			logger.debug("{} updated, nextRun={}, prevRun={}", new Object[]{ schedule, nextRun, prevRun });
			schedulesQueue.add(schedule); // We need to re-add to queue again for next run check.
		}
		logger.debug("Found {}/{} schedules to run.", result.size(), schedules.size());
		return result;
	}

	@Override
	public Date getEarliestRunTime() {
		Schedule schedule = schedulesQueue.peek();
		if (schedule == null)
			return new Date(Long.MAX_VALUE);
		return schedule.getNextRun();
	}
	
	@Override
	public Long generateId(Object data) {
		String name = data.toString();
		Long lastId = objectIds.get(name);
		if (lastId == null) {
			lastId = 0L;
		}
		Long newId = lastId + 1;
		objectIds.put(name, newId);
		return newId;
	}
}

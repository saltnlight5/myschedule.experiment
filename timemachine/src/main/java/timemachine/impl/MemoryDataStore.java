package timemachine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timemachine.Data;
import timemachine.DataStore;
import timemachine.Job;
import timemachine.Schedule;
import timemachine.SchedulerException;

public class MemoryDataStore implements DataStore {
	private static Logger logger = LoggerFactory.getLogger(MemoryScheduler.class);
	private MemoryIdGenerator idGenerator = new MemoryIdGenerator();
	private Map<Long, Job> jobs = Collections.synchronizedMap(new HashMap<Long, Job>());
	private Map<Long, Schedule> schedules = Collections.synchronizedMap(new HashMap<Long, Schedule>());

	@Override
	public void storeData(Data data) {
		if (data instanceof Job) {
			Job job = (Job)data;
			if (job.getId() == null)
				job.setId(idGenerator.generateId(Job.class.getName().toString()));
			jobs.put(job.getId(), job);
		} else if(data instanceof Schedule) {
			Schedule schedule = (Schedule)data;
			if (schedule.getId() == null)
				schedule.setId(idGenerator.generateId(Schedule.class.getName().toString()));
			schedules.put(schedule.getId(), schedule);
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
			result = jobs.remove(id);
		} else if(Schedule.class.equals(dataType)) {
			result = schedules.remove(id);
		} else {
			throw new SchedulerException("Failed to delete data: Invalid dataType=" + dataType.getName());
		}
		if (result == null)
			throw new SchedulerException("Failed to delete data: Id " + id + " not found for dataType=" + dataType.getName());
		
		logger.info("Deleted dataType={} with id={}", dataType.getName(), id);
		return result;
	}
}

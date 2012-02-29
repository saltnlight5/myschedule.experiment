package timemachine.scheduler;

import java.util.List;

import timemachine.scheduler.support.DataStore;
import timemachine.scheduler.support.Service;
import timemachine.scheduler.support.ThreadPool;


public interface Scheduler extends Service {
	public Long getId();
	public String getName();
	
	public void pause();
	public void resume();
	public boolean isPaused();

	public void scheduleJob(Job job);
	public void removeJob(Long jobId);
	
	public void addUserService(Service userService);
	public List<Service> getUserServices();
	
	public DataStore getDataStore();
	public ThreadPool getServiceThreadPool();
	public ThreadPool getJobTaskThreadPool();
}

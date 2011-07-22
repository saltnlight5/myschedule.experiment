package quartz.experiment;

public interface MySchedulerService {
	public void scheduleJobs() throws Exception;
	public void updateJobs() throws Exception;
	public void removeJobs() throws Exception;
	public void showJobs() throws Exception;
}

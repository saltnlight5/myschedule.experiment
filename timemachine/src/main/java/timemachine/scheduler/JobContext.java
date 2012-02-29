package timemachine.scheduler;

public interface JobContext {
	public Scheduler getScheduler();
	public Job getJob();
	public Schedule getSchedule();
}

package timemachine;

public interface JobContext {
	public Scheduler getScheduler();
	public Job getJob();
	public Schedule getSchedule();
}

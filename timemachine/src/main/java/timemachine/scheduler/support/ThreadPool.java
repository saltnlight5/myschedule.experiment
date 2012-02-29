package timemachine.scheduler.support;


public interface ThreadPool extends Service {
	public void execute(Runnable task);
}

package tim.scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DefaultSchedulerConfig implements SchedulerConfig {
	protected Executor executor = Executors.newFixedThreadPool(4);
	
	public Executor getExecutor() {
		return executor;
	}
}

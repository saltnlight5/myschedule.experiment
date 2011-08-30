package tim.scheduler;

import java.util.concurrent.Executor;

public interface SchedulerConfig {
	
	Executor getExecutor();
	
}

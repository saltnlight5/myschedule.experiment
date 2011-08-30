package timemachine;

import java.util.concurrent.Executor;

public interface SchedulerConfig {
	
	Executor getExecutor();
	
}

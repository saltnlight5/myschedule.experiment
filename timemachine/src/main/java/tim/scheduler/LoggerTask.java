package tim.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTask implements Task {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerTask.class);

	@Override
	public void run(SchedulerContext context) {
		logger.info("Job is running.");		
	}
}

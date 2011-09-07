package tim.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerWork implements Work {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerWork.class);

	@Override
	public void run(SchedulerContext context) {
		logger.info("Job is running.");		
	}
}

package timemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRunnableJob implements Runnable {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void run() {
		logger.info("Job is running.");
	}
}

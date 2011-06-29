package myschedule.core.standard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import myschedule.core.Job;
import myschedule.core.RunJobContext;

public class SimpleJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleJob.class);
	
	@Override
	public void execute(RunJobContext runJobContext) {
		logger.debug("No operation job is running with runJobContext: " + runJobContext);
	}

}

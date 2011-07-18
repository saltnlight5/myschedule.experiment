package quartz.experiment;

import java.util.List;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

public class QuartzServerClean extends QuartzServer {
	public static void main(String[] args) {
		new QuartzServerClean().run();
	}
	
	@Override
	protected boolean beforeSchedulerStart() {
		try {
			logger.info("Delete all job and triggers.");
			List<String> names = scheduler.getJobGroupNames();
			for (String name : names) {
				Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher.groupEquals(name));
				for (JobKey key : keys) {
					scheduler.deleteJob(key);
					logger.info(key + " deleted.");
				}
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		return false;
	}
}

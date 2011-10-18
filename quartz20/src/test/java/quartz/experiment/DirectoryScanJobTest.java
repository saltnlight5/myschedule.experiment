package quartz.experiment;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.DirectoryScanJob;
import org.quartz.jobs.DirectoryScanListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryScanJobTest {
	private static final Logger logger = LoggerFactory.getLogger(DirectoryScanJobTest.class);

	public static final String CONFIG = "quartz/experiment/quartz.properties.basic";
	public static final String JOB_NAME = DirectoryScanJobTest.class.getSimpleName();
	public static final String JOB_GROUP = "DEFAULT";

	@Test
	public void testQuartz() throws Exception {
		StdSchedulerFactory sf = new StdSchedulerFactory(CONFIG);
		Scheduler scheduler = sf.getScheduler();

		MyDirListener dirListener = new MyDirListener();
		String dir = System.getProperty("user.home");
		String dirListenerKey = "MyDirListener";
		long minUpdateAge = 5000L;
		scheduler.getContext().put(dirListenerKey, dirListener);
		logger.info("Created dir listener on {}", dir);

		JobDetail jobDetail = newJob(DirectoryScanJob.class)
				.withIdentity(JOB_NAME, JOB_GROUP)
				.usingJobData(DirectoryScanJob.DIRECTORY_NAME, dir)
				.usingJobData(DirectoryScanJob.DIRECTORY_SCAN_LISTENER_NAME, dirListenerKey)
				.usingJobData(DirectoryScanJob.MINIMUM_UPDATE_AGE, minUpdateAge)
				.build();
		Trigger trigger = newTrigger()
				.withIdentity(JOB_NAME)
				.withSchedule(
						simpleSchedule()
						.repeatForever()
						.withIntervalInMilliseconds(3000))
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		logger.info("Job {} scheduled.", jobDetail.getKey());

		logger.info("Start scheduler and let it run for 60 secs.");
		scheduler.start();
		Thread.sleep(60 * 1000L);
		scheduler.shutdown();
		logger.info("Scheduler is done.");
	}
	
	public static class MyDirListener implements DirectoryScanListener {
		private static final Logger logger = LoggerFactory.getLogger(MyDirListener.class);
		@Override
		public void filesUpdatedOrAdded(File[] updatedFiles) {
			logger.info("Updated files detected.");
			for (File file : updatedFiles) {
				logger.info("Found updated file: " + file);
			}
		}		
	}
}

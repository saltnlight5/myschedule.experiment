package quartz.experiment;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;

public class QuartzServer3 extends QuartzServer {
	public static void main(String[] args) {
		new QuartzServer3().run();
	}
	
	@Override
	protected boolean beforeSchedulerStart() {
		try {
			logger.info("Modifying and replacing job1 jobdetail only");
			JobDetail job = SleepJob.createJobDetail("job1", 3500);
			scheduler.addJob(job, true);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}		
		return true;
	}
}

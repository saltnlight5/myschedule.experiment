package integration.myschedule.core.standard;

import myschedule.core.JobSchedule;
import myschedule.core.standard.RepeatSchedule;
import myschedule.core.standard.SimpleJob;
import myschedule.core.standard.StandardScheduler;

import org.junit.Test;

public class StandardSchedulerTest {
	
	@Test
	public void testAddJobInstanceDirectly() throws Exception {
		StandardScheduler scheduler = new StandardScheduler();
		scheduler.start();
		
		JobSchedule jobSchedule = new JobSchedule();
		jobSchedule.setJobName("job1");
		jobSchedule.setJob(new SimpleJob());
		jobSchedule.setSchedule(new RepeatSchedule("job1", 1000));
		scheduler.add(jobSchedule);
		
		Thread.sleep(3000); // we should see at least 3 job runs.
		
		scheduler.destroy();
	}
	
}

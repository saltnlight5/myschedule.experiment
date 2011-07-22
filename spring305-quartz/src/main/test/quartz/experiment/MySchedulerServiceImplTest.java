package quartz.experiment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="MySchedulerServiceImpl2Test-context.xml")
public class MySchedulerServiceImplTest {
	@Autowired
	protected MySchedulerService mySchedulerService;
	
	@Test 
	public void testRemoveJobs() throws Exception {
		mySchedulerService.removeJobs();
	}
	
	//@Test(expected=Exception.class)
	@Test
	public void testScheduleJobsWithExpectedErrors() throws Exception {
		mySchedulerService.showJobs();
		mySchedulerService.scheduleJobs();
	}
	
	@Test 
	public void testShowJobs() throws Exception {
		mySchedulerService.showJobs();
	}
	
}

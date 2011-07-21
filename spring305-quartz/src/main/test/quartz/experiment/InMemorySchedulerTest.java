package quartz.experiment;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class InMemorySchedulerTest {
	@Autowired
	protected Scheduler scheduler;
	
	@Test 
	public void testSpringContext() throws Exception {
		assertThat(scheduler.getSchedulerName(), is("SpringQuartzScheduler"));
	}
}

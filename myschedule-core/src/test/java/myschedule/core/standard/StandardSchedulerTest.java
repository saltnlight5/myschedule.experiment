package myschedule.core.standard;

import myschedule.core.JobSchedule;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StandardSchedulerTest {
	@Test
	public void testStartDestroy() throws Exception {
		// NOTE: Start() should auto call init(), and destroy should auto call stop()
		
		StandardScheduler scheduler = new StandardScheduler();
		assertThat(scheduler.isInitialized(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		
		scheduler.start();
		assertThat(scheduler.isInitialized(), is(true));
		assertThat(scheduler.isStarted(), is(true));
		
		scheduler.destroy();
		assertThat(scheduler.isInitialized(), is(false));
		assertThat(scheduler.isStarted(), is(false));
	}
	
	@Test
	public void testFullLifecycles() throws Exception {
		// NOTE: we should able to life-cycle methods explicitly without problems
		
		StandardScheduler scheduler = new StandardScheduler();
		assertThat(scheduler.isInitialized(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		
		scheduler.init();
		assertThat(scheduler.isInitialized(), is(true));
		assertThat(scheduler.isStarted(), is(false));
		
		scheduler.start();
		assertThat(scheduler.isInitialized(), is(true));
		assertThat(scheduler.isStarted(), is(true));
		
		scheduler.stop();
		assertThat(scheduler.isInitialized(), is(true));
		assertThat(scheduler.isStarted(), is(false));
		
		scheduler.destroy();
		assertThat(scheduler.isInitialized(), is(false));
		assertThat(scheduler.isStarted(), is(false));
	}
	
	@Test
	public void testAddJobInstanceDirectly() throws Exception {
		StandardScheduler scheduler = new StandardScheduler();
		scheduler.start();
		
		JobSchedule job = new JobSchedule();
		job.setJobName("job1");
		job.setJob(new SimpleJob());
		job.setSchedule(new RepeatSchedule("job1", 1000));
		scheduler.add(job);
				
		scheduler.destroy();
	}
}

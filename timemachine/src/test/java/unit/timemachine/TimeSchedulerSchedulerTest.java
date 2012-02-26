package unit.timemachine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import timemachine.Scheduler;
import timemachine.TimeMachineScheduler;

public class TimeSchedulerSchedulerTest {
	
	@Test
	public void testSchedulerLifecycles() throws Exception {
		Scheduler scheduler = new TimeMachineScheduler();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		assertThat(scheduler.isPaused(), is(false));
		
		scheduler.init();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(false));
		assertThat(scheduler.isPaused(), is(false));
		
		scheduler.start();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(true));
		assertThat(scheduler.isPaused(), is(false));

		scheduler.pause();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(true));
		assertThat(scheduler.isPaused(), is(true));

		scheduler.resume();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(true));
		assertThat(scheduler.isPaused(), is(false));
				
		scheduler.stop();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(false));
		assertThat(scheduler.isPaused(), is(false));
		
		scheduler.destroy();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		assertThat(scheduler.isPaused(), is(false));
	}
	
	@Test
	public void testSchedulerStartStop() throws Exception {
		Scheduler scheduler = new TimeMachineScheduler();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		assertThat(scheduler.isPaused(), is(false));
		
		scheduler.start();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(true));
		assertThat(scheduler.isPaused(), is(false));
		
		scheduler.destroy();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		assertThat(scheduler.isPaused(), is(false));
	}
	
	@Test
	public void testSchedulerReStart() throws Exception {
		Scheduler scheduler = new TimeMachineScheduler();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		
		// should be same as above.
		scheduler.start();
		scheduler.destroy();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
		
		// we should able to re init+start with same instance
		scheduler.start();
		scheduler.stop();
		assertThat(scheduler.isInited(), is(true));
		assertThat(scheduler.isStarted(), is(false));
		
		// we should able to re start and not need to init
		scheduler.start();
		scheduler.destroy();
		assertThat(scheduler.isInited(), is(false));
		assertThat(scheduler.isStarted(), is(false));
	}
}

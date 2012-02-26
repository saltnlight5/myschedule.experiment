package timemachine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MemorySchedulerTest {
	
	@Test
	public void testSchedulerLifecycles() throws Exception {
		Scheduler scheduler = new MemoryScheduler();
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
		Scheduler scheduler = new MemoryScheduler();
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
		Scheduler scheduler = new MemoryScheduler();
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
	
	@Test
	public void testScheduleJob() {
		// We will schedule/add jobs without starting the scheduler.
		Scheduler scheduler = new MemoryScheduler();
		scheduler.init();
		
		Job job = new Job();
		job.setTaskClass(LoggerJobTask.class);
		job.addSchedule(new FixedIntervalSchedule());
		scheduler.schedule(job);
		
		// second job
		job = new Job();
		job.setTaskClass(LoggerJobTask.class);
		job.addSchedule(new FixedIntervalSchedule());
		scheduler.schedule(job);
	}
	
	@Test
	public void testSchedulerRunner() throws Exception {
		Scheduler scheduler = new MemoryScheduler();
		scheduler.start();
		Thread.sleep(3000L);
		
		// Schedule a job and let scheduler run for 5 secs.
		Job job = new Job();
		job.setTaskClass(LoggerJobTask.class);
		job.addSchedule(new FixedIntervalSchedule(2, ScheduleUnit.SECONDS));
		scheduler.schedule(job);
		Thread.sleep(10000L);
		
		scheduler.destroy();
	}
}

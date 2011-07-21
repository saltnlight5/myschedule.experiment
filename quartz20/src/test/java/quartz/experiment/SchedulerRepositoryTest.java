package quartz.experiment;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.quartz.Scheduler;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerRepositoryTest {
	/**
	 * Within the same JVM, you can not instantiate multiple Quartz scheduler with the same name, in regardless of the
	 * instanceId! No warning is given, and it will reuse existing scheduler when using StdSchedulerFactory, even if you
	 * supply different properties file!
	 * 
	 * This means you can not have two cluster nodes scheduler instances in one JVM.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMultipleSchedulers() throws Exception {
		StdSchedulerFactory sf1 = new StdSchedulerFactory("quartz/experiment/quartz.properties.basic1");
		Scheduler scheduler1 = sf1.getScheduler();
		assertThat(scheduler1.getSchedulerName(), is("BasicScheduler"));
		assertThat(scheduler1.getSchedulerInstanceId(), is("NODE1"));

		Scheduler schedulerFromRepo1 = SchedulerRepository.getInstance().lookup("BasicScheduler");
		assertThat(schedulerFromRepo1.hashCode(), is(scheduler1.hashCode()));

		StdSchedulerFactory sf2 = new StdSchedulerFactory("quartz/experiment/quartz.properties.basic2");
		Scheduler scheduler2 = sf2.getScheduler();
		assertThat(scheduler2.getSchedulerName(), is("BasicScheduler"));
		// assertThat(scheduler2.getSchedulerInstanceId(), is("NODE2")); // It failed here!!!
		assertThat(scheduler2.getSchedulerInstanceId(), is("NODE1")); // It still NODE1!
		assertThat(scheduler2.hashCode(), is(scheduler1.hashCode())); // They are the same instance! No warning!

		Scheduler schedulerFromRepo2 = SchedulerRepository.getInstance().lookup("BasicScheduler");
		assertThat(schedulerFromRepo2.hashCode(), is(scheduler1.hashCode())); // same instance!

		scheduler1.shutdown();
		scheduler2.shutdown();
	}
}

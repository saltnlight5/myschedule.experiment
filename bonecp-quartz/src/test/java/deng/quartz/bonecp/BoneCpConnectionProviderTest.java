package deng.quartz.bonecp;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class BoneCpConnectionProviderTest {

	@Test
	public void testSchedulerWithBoneCp() throws Exception {
		StdSchedulerFactory factory = new StdSchedulerFactory("deng/quartz/bonecp/quartz.properties");
		Scheduler scheduler = factory.getScheduler();
		
		scheduler.start();
		
		Thread.sleep(10000L);
	}
}

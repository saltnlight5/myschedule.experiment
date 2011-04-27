//package quartz.experiment;
//
//import static org.hamcrest.Matchers.*;
//
//import java.util.Date;
//
//import org.junit.Test;
//import org.quartz.DateBuilder;
//import org.quartz.Scheduler;
//import org.quartz.DateBuilder.IntervalUnit;
//import org.quartz.impl.StdSchedulerFactory;
//
//
//public class QuartzServiceTest {
//
//	@Test
//	public void testOnetimeJob() throws Exception {
//		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//		QuartzService quartzService = new QuartzService();
//		quartzService.setScheduler(scheduler);
//		
//		quartzService.scheduleOnetimeJob("job1", SimpleJob.class);
//		quartzService.start();
//		
//		Thread.sleep(5000);
//		quartzService.stop();
//	}
//	
//
//	@Test
//	public void testOnetimeJobWithStartTime() throws Exception {
//		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//		QuartzService quartzService = new QuartzService();
//		quartzService.setScheduler(scheduler);
//		
//		Date startTime = DateBuilder.futureDate(3, IntervalUnit.SECOND);
//		quartzService.scheduleOnetimeJob("job2", SimpleJob.class, startTime);
//		quartzService.start();
//		
//		Thread.sleep(5000);
//		quartzService.stop();
//	}
//}

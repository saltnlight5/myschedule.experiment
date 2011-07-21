package quartz.experiment;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/** QuartzExample
 *
 * @author Zemian Deng
 */
public class QuartzExample {
  public static void main(String[] args) throws Exception {
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

    JobDetail job = new JobDetail("job1", SimpleJob.class);
    SimpleTrigger trigger = new SimpleTrigger("job1");

    scheduler.scheduleJob(job, trigger);
    
    scheduler.start();
    Thread.sleep(3000L);
  }
}

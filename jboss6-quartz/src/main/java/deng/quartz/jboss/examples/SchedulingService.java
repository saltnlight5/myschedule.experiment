package deng.quartz.jboss.examples;

import javax.jms.Session;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.quartz.*;
import org.quartz.jobs.ee.jms.JmsHelper;
import org.quartz.jobs.ee.jms.SendDestinationMessageJob;

public class SchedulingService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Scheduler scheduler;
	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
		 
	/** Create a one-time job for SendDestinationMessageJob to scheduler for testing. */
	public void scheduleOnetimeJmsJob() {
		JobDataMap jobData = new JobDataMap();
		jobData.put(JmsHelper.JMS_CONNECTION_FACTORY_JNDI, "java:/ConnectionFactory");
		jobData.put(JmsHelper.JMS_DESTINATION_JNDI, "java:/queue/QuartzQueue");
		jobData.put(JmsHelper.JMS_USE_TXN, "true");
		jobData.put(JmsHelper.JMS_ACK_MODE, Session.AUTO_ACKNOWLEDGE);
		jobData.put(JmsHelper.JMS_MSG_FACTORY_CLASS_NAME, SimpleJmsMessageFactory.class.getName());
		jobData.put(JmsHelper.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		jobData.put(JmsHelper.PROVIDER_URL, "localhost:1099");
		JobDetail job = JobBuilder.newJob(SendDestinationMessageJob.class).withIdentity("jmsJob").usingJobData(jobData).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("jmsJob").build();
		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

}

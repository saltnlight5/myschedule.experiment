package quartz.experiment.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;
import org.springframework.scheduling.quartz.JobDetailAwareTrigger;
import org.springframework.scheduling.quartz.ResourceLoaderClassLoadHelper;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/** 
 * Extension to Spring's  SchedulerFactoryBean that fix two issues:
 * 
 * 1) It throws error whenever jobSchedulerDataLocation(s) is used. This is because Spring is using
 * Quartz's org.quartz.xml.JobSchedulingDataProcessor, which only exists in Quartz 1.6.x to 1.7.x.
 * Starting Quartz 1.8.x, it renamed the class to org.quartz.xml.XMLSchedulingDataProcessor. So Spring's
 * SchedulerAccessor class will always through this missing class exception. But then the exception msg
 * is truncated, so user can not see the real cause. We will fix by using the 
 * org.quartz.xml.XMLSchedulingDataProcessor class here.
 * 
 * 2) Re-add and fix the exception truncation and not break the chain.
 * 
 * 3) We use CascadingClassLoadHelperExt to avoid offline loading schema xsd failure issue.
 * 
 * <p>
 * Note that the Quartz's new org.quartz.xml.XMLSchedulingDataProcessor class seems to ignore the 
 * overWriteExistingJobs parameter in one of it's processFileAndScheduleJobs method! We should file a bug
 * for this. 
 *
 * @author Zemian Deng
 */
public class SchedulerExtFactoryBean extends SchedulerFactoryBean {

	protected PlatformTransactionManager transactionManager;
	
	private boolean overwriteExistingJobs = false;
	
	private String[] jobSchedulingDataLocations;

	private List<JobDetail> jobDetails;

	private Map<String, Calendar> calendars;

	private List<Trigger> triggers;
	
	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setOverwriteExistingJobs(boolean overwriteExistingJobs) {
		this.overwriteExistingJobs = overwriteExistingJobs;
		super.setOverwriteExistingJobs(overwriteExistingJobs);
	}
	
	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setJobSchedulingDataLocation(String jobSchedulingDataLocation) {
		this.jobSchedulingDataLocations = new String[] {jobSchedulingDataLocation};
		super.setJobSchedulingDataLocation(jobSchedulingDataLocation);
	}
	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setJobSchedulingDataLocations(String[] jobSchedulingDataLocations) {
		this.jobSchedulingDataLocations = jobSchedulingDataLocations;
		super.setJobSchedulingDataLocations(jobSchedulingDataLocations);
	}
	
	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
		super.setTransactionManager(transactionManager);
	}
	
	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setJobDetails(JobDetail[] jobDetails) {
		// Use modifiable ArrayList here, to allow for further adding of
		// JobDetail objects during autodetection of JobDetailAwareTriggers.
		this.jobDetails = new ArrayList<JobDetail>(Arrays.asList(jobDetails));
		super.setJobDetails(jobDetails);
	}

	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setCalendars(Map<String, Calendar> calendars) {
		this.calendars = calendars;
		super.setCalendars(calendars);
	}

	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	public void setTriggers(Trigger[] triggers) {
		this.triggers = Arrays.asList(triggers);
		super.setTriggers(triggers);
	}
		
	/**
	 * Register jobs and triggers (within a transaction, if possible).
	 */
	protected void registerJobsAndTriggers() throws SchedulerException {
		TransactionStatus transactionStatus = null;
		if (this.transactionManager != null) {
			transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		}
		try {

			if (this.jobSchedulingDataLocations != null) {
				logger.info("Using CascadingClassLoadHelperExt to use default and ResourceLoaderClassLoadHelper classLoadHelpers");
				ClassLoadHelper springclhelper = new ResourceLoaderClassLoadHelper(this.resourceLoader);
				CascadingClassLoadHelperExt clhelper = new CascadingClassLoadHelperExt();
				clhelper.addClassLoadHelper(springclhelper);
				clhelper.addDefaultClassLoadHelpers();
				clhelper.initialize();
				
				XMLSchedulingDataProcessor dataProcessor = new XMLSchedulingDataProcessor(clhelper);
				for (String location : this.jobSchedulingDataLocations) {
					logger.info("Loading JobSchedulingData location=" + location);
					dataProcessor.processFileAndScheduleJobs(location, getScheduler());
				}
			}

			// Register JobDetails.
			if (this.jobDetails != null) {
				for (JobDetail jobDetail : this.jobDetails) {
					addJobToScheduler(jobDetail);
				}
			}
			else {
				// Create empty list for easier checks when registering triggers.
				this.jobDetails = new LinkedList<JobDetail>();
			}

			// Register Calendars.
			if (this.calendars != null) {
				for (String calendarName : this.calendars.keySet()) {
					Calendar calendar = this.calendars.get(calendarName);
					getScheduler().addCalendar(calendarName, calendar, true, true);
				}
			}

			// Register Triggers.
			if (this.triggers != null) {
				for (Trigger trigger : this.triggers) {
					addTriggerToScheduler(trigger);
				}
			}
		}

		catch (Throwable ex) {
			if (transactionStatus != null) {
				try {
					this.transactionManager.rollback(transactionStatus);
				}
				catch (TransactionException tex) {
					logger.error("Job registration exception overridden by rollback exception", ex);
					throw tex;
				}
			}
			if (ex instanceof SchedulerException) {
				throw (SchedulerException) ex;
			}
			if (ex instanceof Exception) {
				throw new SchedulerException("Registration of jobs and triggers failed: " + ex.getMessage(), ex);
			}
			throw new SchedulerException("Registration of jobs and triggers failed: " + ex.getMessage(), ex);
		}

		if (transactionStatus != null) {
			this.transactionManager.commit(transactionStatus);
		}
	}
	
	/** 
	 * Over shadow the parent method so we have access in this class.
	 */
	protected boolean addJobToScheduler(JobDetail jobDetail) throws SchedulerException {
		if (this.overwriteExistingJobs ||
		    getScheduler().getJobDetail(jobDetail.getKey()) == null) {
			getScheduler().addJob(jobDetail, true);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Over shadow the parent method so we have access in this class.
	 */
	protected boolean addTriggerToScheduler(Trigger trigger) throws SchedulerException {
		boolean triggerExists = (getScheduler().getTrigger(trigger.getKey()) != null);
		if (!triggerExists || this.overwriteExistingJobs) {
			// Check if the Trigger is aware of an associated JobDetail.
			if (trigger instanceof JobDetailAwareTrigger) {
				JobDetail jobDetail = ((JobDetailAwareTrigger) trigger).getJobDetail();
				// Automatically register the JobDetail too.
				if (!this.jobDetails.contains(jobDetail) && addJobToScheduler(jobDetail)) {
					this.jobDetails.add(jobDetail);
				}
			}
			if (!triggerExists) {
				try {
					getScheduler().scheduleJob(trigger);
				}
				catch (ObjectAlreadyExistsException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Unexpectedly found existing trigger, assumably due to cluster race condition: " +
								ex.getMessage() + " - can safely be ignored");
					}
					if (this.overwriteExistingJobs) {
						getScheduler().rescheduleJob(trigger.getKey(), trigger);
					}
				}
			}
			else {
				getScheduler().rescheduleJob(trigger.getKey(), trigger);
			}
			return true;
		}
		else {
			return false;
		}
	}
}

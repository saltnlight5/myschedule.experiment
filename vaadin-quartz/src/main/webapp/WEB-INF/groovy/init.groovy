logger.debug("Initializing webapp.")

/* // Create and start a scheduler.
import org.quartz.*
import org.quartz.impl.*
import org.quartz.jobs.*
scheduler = new StdSchedulerFactory().getScheduler()
servletContext.setAttribute("scheduler", scheduler)
scheduler.start() */

import myschedule.quartz.extra.*
scheduler = new SchedulerTemplate()
scheduler.start()

// Save the scheduler as a map.
schedulersMap = [:]
schedulersMap.put(scheduler.getSchedulerNameAndId(), scheduler)
servletContext.setAttribute("schedulersMap", schedulersMap)

// Schedule some sample jobs -- from myschedule's jobSamples.groovy
// ================================================================

// Simple Jobs
// ===========

// Using myschedule.quartz.extra.SchedulerTemplate API
// ===================================================
import myschedule.quartz.extra.job.LoggerJob
import myschedule.quartz.extra.job.ScriptingJob

// Schedule hourly job
scheduler.scheduleSimpleJob("hourlyJob", -1, 60 * 60 * 1000, LoggerJob.class)

// Schedule minutely job
scheduler.scheduleSimpleJob("minutelyJob", -1, 60 * 1000, LoggerJob.class)

// Schedule secondly job
scheduler.scheduleSimpleJob("secondlyJob", -1, 1000, LoggerJob.class)

// Schedule secondly job that repeat total of 3 times.
scheduler.scheduleSimpleJob("secondlyJobRepeat3", 3, 1000, LoggerJob.class)

// Schedule onetime job that run immediately
scheduler.scheduleSimpleJob("onetimeJob", 1, 0, LoggerJob.class)

// Schedule hourly job with job data and start time of 20s delay.
scheduler.scheduleSimpleJob("hourlyJobWithStartTimeDelay", -1, 60 * 60 * 1000, ScriptingJob.class,
		scheduler.mkMap(
			'ScriptEngineName', 'Groovy',
			'ScriptText', '''
				logger.info("I am a script job...")
				sleep(700L)
				logger.info("I am done.")
			'''
		),
		new Date(System.currentTimeMillis() + 20 * 1000))

// Schedule one job with multiple triggers
import org.quartz.*
import myschedule.quartz.extra.job.LoggerJob
job = scheduler.createJobDetail(JobKey.jobKey("jobWithMutltipleTriggers"), LoggerJob.class, true, null)
scheduler.addJob(job, false)
trigger1 = scheduler.createSimpleTrigger("trigger1", -1, 60 * 60 * 1000) // hourly trigger
trigger1.setJobKey(job.getKey())
scheduler.scheduleJob(trigger1)
trigger2 = scheduler.createSimpleTrigger("trigger2", -1, 60 * 1000) // minutely trigger
trigger2.setJobKey(job.getKey())
scheduler.scheduleJob(trigger2)

// Using Java org.scheduler.Scheduler API
// ===================================================
import org.quartz.*
import myschedule.quartz.extra.job.LoggerJob
quartzScheduler = scheduler.getScheduler()
job = JobBuilder
	.newJob(LoggerJob.class)
	.withIdentity("hourlyJob2")
	.build()
trigger = TriggerBuilder
	.newTrigger()
	.withSchedule(
		SimpleScheduleBuilder.repeatHourlyForever())
	.build()
quartzScheduler.scheduleJob(job, trigger)

// Fill up more sample jobs
100.times{ i->
	scheduler.scheduleSimpleJob("SampleJob_$i", -1, 60 * 60 * 1000, LoggerJob.class)
}


// Groovy Extension to Vaadin library
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
Button.metaClass.addClickListener = { closure ->
	delegate.addListener(new Button.ClickListener() {
		@Override void buttonClick(Button.ClickEvent event) {
			closure.call(event)
		}
	})
}

Tree.metaClass.addValueChangeListener = { closure ->
	delegate.addListener(new Property.ValueChangeListener() {
		@Override void valueChange(Property.ValueChangeEvent event){
			closure.call(event)
		}
	})
}

Properties.metaClass.'static'.metaClass.loadFromString = { str ->
	def reader = new StringReader(str)
	def props = new Properties()
	props.load(reader)
	return props
}

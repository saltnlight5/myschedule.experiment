package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
import org.quartz.*
import myschedule.quartz.extra.*
/**
A trigger bean headers:
	calendarName
	description
	endTime
	finalFireTime
	jobDataMap
	jobKey
	key
	misfireInstruction
	nextFireTime
	previousFireTime
	priority
	scheduleBuilder
	startTime
	triggerBuilder
*/
class TriggersTable extends Table {
	TriggersTable(scheduler) {
		setContainerDataSource(createDataContainer(scheduler))
		setColumnCollapsingAllowed(true)
		setColumnCollapsed("calendarName", true)
		setColumnCollapsed("endTime", true)
		setColumnCollapsed("description", true)
		setColumnCollapsed("finalFireTime", true)
		setColumnCollapsed("jobDataMap", true)
		setColumnCollapsed("scheduleBuilder", true)
		setColumnCollapsed("startTime", true)
		setColumnCollapsed("triggerBuilder", true)
		
		setSizeFull()
	}

	def createDataContainer(scheduler) {
		def data = new BeanItemContainer(Trigger.class)
		scheduler.getAllTriggers().each{ trigger -> 
			data.addItem(trigger)
		}
		return data
	}
}

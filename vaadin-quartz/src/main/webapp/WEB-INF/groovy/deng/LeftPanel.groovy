package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
class LeftPanel extends Tree {
	String schedulersItemId = "Quartz Schedulers"
	String toolsItemId = "Tools"
	HierarchicalContainer treeData
	
	LeftPanel(scheduler) {
		treeData = new HierarchicalContainer()
		setContainerDataSource(treeData)
		initTreeData(scheduler)

		// Expand out all schedulers in a tree
		expandItemsRecursively(schedulersItemId)
		expandItemsRecursively(toolsItemId)
	}

	def initTreeData(scheduler) {

		// Schedulers item
		treeData.addItem(schedulersItemId)
		def schedulerName = scheduler.getSchedulerNameAndId()
		createNewScheduler(schedulerName)
		
		// Tools item
		treeData.addItem(toolsItemId)
		createSubItem("Scripting Console", toolsItemId)
		createSubItem("Cron Builder", toolsItemId)
	}

	def createNewScheduler(schedulerName) {
		createSubItem(schedulerName, schedulersItemId)
	}

	def createSubItem(itemId, parentId) {
		treeData.addItem(itemId)
		treeData.setParent(itemId, parentId)
		treeData.setChildrenAllowed(itemId, false)
	}
}
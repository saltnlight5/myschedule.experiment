package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
class LeftPanel extends Tree {
	String schedulersItemId = "Quartz Schedulers"
	String toolsItemId = "Tools"
	HierarchicalContainer treeData
	MainWindow mainWindow
	
	LeftPanel(mainWindow) {
		this.mainWindow = mainWindow

		// Setup the scheduler tree
		def schedulersMap = mainWindow.schedulersMap
		treeData = new HierarchicalContainer()
		setContainerDataSource(treeData)
		initTreeData(schedulersMap)
		
		// Congifure tree
		setSelectable(true)
		setImmediate(true)
		def itemId = schedulersMap.keySet().iterator().next()
		select(itemId)
		// initialize the right panel display with default selected scheduler
		def rightPanel = mainWindow.rightPanel
		rightPanel.resetSchedulerTabs(itemId)

		// What to do when user select a scheduler name on the tree
		addValueChangeListener{ event ->
			def selectedItemId = getValue()
			if (selectedItemId != null) {
				mainWindow.rightPanel.resetSchedulerTabs(selectedItemId)
			}
		}

		// Expand out all scheduler names and tools in this tree
		expandItemsRecursively(schedulersItemId)
		expandItemsRecursively(toolsItemId)
	}

	def initTreeData(schedulersMap) {
		// Schedulers item
		treeData.addItem(schedulersItemId)
		schedulersMap.each { schedulerName, scheduler ->
			createNewScheduler(schedulerName)
		}
		
		// Tools item
		treeData.addItem(toolsItemId)
		createSubItem("Scripting Console", toolsItemId)
		createSubItem("Cron Builder", toolsItemId)
	}

	def schedulerExists(schedulerName) {
		return treeData.getItem(schedulerName) != null
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
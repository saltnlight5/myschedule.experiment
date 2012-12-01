package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
class RightPanel extends TabSheet {
	MainWindow mainWindow
	List<TabSheet.Tab> tabs
	
	RightPanel(mainWindow) {
		this.mainWindow = mainWindow
		this.tabs = []

		setSizeFull()
	}

	def resetSchedulerTabs(schedulerItemId) {
		// Remove all tabs first
		if (tabs.size() > 0)
			tabs.each { tab -> removeTab(tab) }

		// Add new tabs again
		def scheduler = mainWindow.schedulersMap.get(schedulerItemId)
		def tab = addTab(new TriggersTable(scheduler), "Triggers")
		tabs.add(tab)
	}
}
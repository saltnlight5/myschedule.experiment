package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.terminal.*
import myschedule.quartz.extra.*
class MainWindow extends Window {
	HorizontalLayout toolbar
	HorizontalSplitPanel splitPanel
	LeftPanel leftPanel
	RightPanel rightPanel
	Map<String, SchedulerTemplate> schedulersMap

	MainWindow(schedulersMap) {
		this.schedulersMap = schedulersMap

		// Use window's layout as a component container.
		def content = getLayout()
		content.setSizeFull()
		content.setSpacing(true)
		
		// Create the toolbar
		rightPanel = new RightPanel(this) // This order is important, since leftPanel depends on rightPanel.
		leftPanel = new LeftPanel(this)
		toolbar = new HorizontalLayout()
		content.addComponent(toolbar)
		toolbar.setSpacing(true)
		toolbar.addComponent(createNewSchedulerButton(leftPanel))

		// Create the content
		splitPanel = new HorizontalSplitPanel()
		content.addComponent(splitPanel)
		content.setExpandRatio(splitPanel, 1f)
		splitPanel.addComponent(leftPanel)
		splitPanel.addComponent(rightPanel)
		splitPanel.setSplitPosition(20, Sizeable.UNITS_PERCENTAGE)
	}

	def createNewSchedulerButton(leftPanel) {
		def button = new Button("New Scheduler")
		button.addClickListener{ event ->
			addWindow(new SchedulerConfigEditor(this)) 
		}
		return button
	}
}

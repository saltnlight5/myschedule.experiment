package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.terminal.*
class MainWindow extends Window {
	HorizontalLayout toolbar
	HorizontalSplitPanel splitPanel
	LeftPanel leftPanel
	RightPanel rightPanel

	MainWindow(scheduler) {
		// Use window's layout as a component container.
		def content = getLayout()
		content.setSizeFull()
		content.setSpacing(true)
		
		// Create the toolbar
		leftPanel = new LeftPanel(scheduler)
		rightPanel = new RightPanel(scheduler)
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
		button.addListener{ event ->
			addWindow(new NewSchedulerWindow(this)) 
		}
		return button
	}
}

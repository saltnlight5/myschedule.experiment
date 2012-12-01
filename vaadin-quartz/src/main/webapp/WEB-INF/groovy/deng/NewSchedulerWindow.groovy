package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.terminal.*
import myschedule.quartz.extra.*
class NewSchedulerWindow extends Window {
	TextArea editor
	MainWindow mainWindow

	public NewSchedulerWindow(mainWindow) {
		this.mainWindow = mainWindow

		// Setup this window
		setModal(true)
		setWidth("90%")
		setHeight("90%")

		// Get this window's layout as component container.
		def content = getLayout()
		content.setSizeFull()
		content.setSpacing(true)

		def editor = createEditorTextArea()
		content.addComponent(editor)
		content.setExpandRatio(editor, 1f)

		def toolbar = new HorizontalLayout()
		toolbar.setSpacing(true)
		content.addComponent(toolbar)
		toolbar.addComponent(createSaveButton())
		toolbar.addComponent(createCancelButton())
	}

	def closeWindow() {
		mainWindow.removeWindow(this)
	}

	def createEditorTextArea() {
		this.editor = new TextArea("Quartz Scheduler Config Properties")
		editor.setSizeFull()
		return editor
	}

	def createSaveButton() {
		def button = new Button("Save")
		button.addClickListener{ event -> 
			try {
				processNewSchedulerSettings()
				closeWindow()
			} catch (Exception e) {
				showError(e.getMessage())
			}
		}
		return button
	}

	def showError(msg) {
		showNotification(null, msg, Window.Notification.TYPE_ERROR_MESSAGE)
	}

	def createCancelButton() {
		def button = new Button("Cancel")
		button.addClickListener{ event -> closeWindow() }
		return button
	}

	def processNewSchedulerSettings() {
		def text = editor.getValue()
		def props = Properties.loadFromString(text)
		def name = props.getProperty("org.quartz.scheduler.instanceName", "DefaultQuartzScheduler")
		def id = props.getProperty("org.quartz.scheduler.instanceId", "NON_CLUSTERED")
		def itemId = name + '_$_' + id
		def leftPanel = mainWindow.leftPanel

		if (leftPanel.schedulerExists(itemId))
			throw new RuntimeException("Scheduler instance name and id '$itemId' already exists.")
		
		def scheduler = new SchedulerTemplate(props)
		mainWindow.schedulersMap.put(itemId, scheduler)
		leftPanel.createNewScheduler(itemId)
	}
}
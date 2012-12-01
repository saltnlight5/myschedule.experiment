package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
class QuartzApplication extends Application {
	void init() {
		def servletContext = getContext().getHttpSession().getServletContext()
		def scheduler = servletContext.getAttribute("scheduler")
		setMainWindow(new MainWindow(scheduler))
	}
}

import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.terminal.*
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

	def createEditorTextArea() {
		this.editor = new TextArea("Quartz Scheduler Config Properties")
		editor.setSizeFull()
		return editor
	}

	def createSaveButton() {
		def button = new Button("Save")
		button.addListener{ event -> 
			processNewSchedulerSettings()
			closeWindow()
		}
		return button
	}

	def createCancelButton() {
		def button = new Button("Cancel")
		button.addListener{ event -> closeWindow() }
		return button
	}

	def processNewSchedulerSettings() {
		def text = editor.getValue()
		def props = Properties.loadFromString(text)
		def name = props.getProperty("org.quartz.schedulerName", "DefaultQuartzScheduler")
		mainWindow.leftPanel.createNewScheduler(name)
	}

	def closeWindow() {
		mainWindow.removeWindow(this)
	}
}
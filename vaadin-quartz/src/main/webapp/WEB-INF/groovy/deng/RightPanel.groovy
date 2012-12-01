package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
class RightPanel extends TabSheet {
	RightPanel(scheduler) {
		addTab(new TriggersTable(scheduler), "Triggers")
		setSizeFull()
	}
}
package deng
import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.data.*
import com.vaadin.data.util.*
class QuartzApplication extends Application {
	void init() {
		def servletContext = getContext().getHttpSession().getServletContext()
		def schedulersMap = servletContext.getAttribute("schedulersMap")
		setMainWindow(new MainWindow(schedulersMap))
	}
}

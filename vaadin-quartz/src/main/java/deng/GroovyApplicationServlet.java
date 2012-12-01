package deng;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyResourceLoader;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.servlet.ServletException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.slf4j.*;

/**
 * This class is originally from https://github.com/halls/groovy-vaadin.
 *
 * 2012/11/21
 * - Added logger.
 * - Added support for relative scriptsPath using "/WEB-INF" prefix.
 * 
 * Created 24.01.2010 23:33:33
 */
public class GroovyApplicationServlet extends ApplicationServlet {
	private static Logger logger = LoggerFactory.getLogger(GroovyApplicationServlet.class);
	private GroovyClassLoader cl = null;

	@Override
	protected ClassLoader getClassLoader() throws ServletException {
		if (cl == null) {
			String scriptsPath = getServletConfig().getInitParameter("scriptsPath");
			if (scriptsPath.startsWith("/WEB-INF")) {
				scriptsPath = getServletContext().getRealPath(scriptsPath);
			}
			if (scriptsPath != null) {
				logger.info("Creating Groovy class loader from scriptsPath: " + scriptsPath);
				final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
				compilerConfiguration.setRecompileGroovySource(true);
				cl = new GroovyClassLoader(super.getClassLoader(), compilerConfiguration);
				final String scriptsPathFinal = scriptsPath;
				cl.setResourceLoader(new GroovyResourceLoader() {
					public URL loadGroovySource(final String name) throws MalformedURLException {
						return (URL) AccessController.doPrivileged(new PrivilegedAction() {
							public Object run() {
								String filename = name.replace('.', '/') 
										+ compilerConfiguration.getDefaultScriptExtension();
								try {
									final File file = new File(scriptsPathFinal + "/" + filename);
									if (!file.exists() || !file.isFile()) {
										return null;
									}
									String path = "file:///" + scriptsPathFinal + "/" + filename;
									logger.debug("Getting class url: " + path);
									return new URL(path);
								} catch (MalformedURLException e) {
									logger.warn("Problem loading filename: " + filename, e);
									return null;
								}
							}
						});
					}
				});
			}
		}
		return cl;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Class<? extends Application> getApplicationClass() {
		final String applicationClassName = getServletConfig().getInitParameter("application");
		if (applicationClassName == null) {
			throw new RuntimeException("Application not specified in servlet parameters.");
		}
		try {
			//logger.debug("Loading Vaadin application from Groovy class: " + applicationClassName);
			getClassLoader(); // Ensure cl is loaded.
			return (Class<? extends Application> ) cl.loadClass(applicationClassName, true, false);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load Vaadin application from Groovy class: " + applicationClassName, e);
		}
	}
}
package deng;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.slf4j.*;

/** 
 * A servlet context listener to load and/or unload any Groovy scripts.
 *
 * @author Zemian Deng
 */
public class GroovyContextListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(GroovyContextListener.class);
	private String[] initScripts;
	private String[] destroyScripts;
	private GroovyScriptEngine scriptEngine;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();

		String initScriptsStr = ctx.getInitParameter("initScripts");
		if (initScriptsStr != null && !initScriptsStr.trim().equals(""))
			initScripts = initScriptsStr.split(",");
		String destroyScriptsStr = ctx.getInitParameter("destroyScripts");
		if (destroyScriptsStr != null && !destroyScriptsStr.trim().equals(""))
			destroyScripts = destroyScriptsStr.split(",");

		try {
			String webappDir = ctx.getRealPath("");
			String[] dirs = new String[] { webappDir };
			scriptEngine = new GroovyScriptEngine(dirs);

			if (initScripts != null) {
				Binding binding = createBinding(ctx);
				for (String filename : initScripts)
					runScript(filename, binding);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to init webapp with scripts.", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			ServletContext ctx = sce.getServletContext();
			if (destroyScripts != null) {
				Binding binding = createBinding(ctx);
				for (String filename : destroyScripts)
					runScript(filename, binding);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to destroy webapp with scripts.", e);
		}
	}

	private Binding createBinding(ServletContext ctx) {
		Logger scriptLogger = LoggerFactory.getLogger(GroovyContextListener.class.getName() + "_script");
		Binding binding = new Binding();
		binding.setVariable("logger", scriptLogger);
		binding.setVariable("servletContext", ctx);
		return binding;
	}

	private void runScript(String filename, Binding binding) throws Exception {
		if (filename.startsWith("/"))
			filename = filename.substring(1);
		logger.info("Running destroy script: " + filename);
		scriptEngine.run(filename, binding);
	}
}

package org.quartz.extra;

import java.io.InputStream;
import java.net.URL;

import org.xeustechnologies.jcl.JarClassLoader;

/**
 * An extension to CascadingClassLoadHelperExt that support JarClassLoader. If this class failed to load
 * a class or resource, it will delegate to parent.
 * 
 * <p>
 * Due to Quartz has no way to set this class fields, you may customize the jarPaths by using sys prop.
 * Eg: "-DjarPaths=path1.jar,path2.jar,path3.jar".
 * 
 * <p>
 * This JCL may load what's originally started in the JVM. Set loadOrigJavaClassPath=true sys prop to use 
 * this feature.
 * 
 * <p>
 * When running standalone Java SE, the JCL uses a separated isolated log4j settings that will suppress 
 * Quartz log output by default. You need to set "-Djcl.isolateLogging=false" in sys prop to see Quartz logging properly.
 * 
 * @author Zemian Deng <saltnlight5@gmail.com>
 *
 */
public class JclClassLoadHelper2 extends CascadingClassLoadHelperExt {
	
	protected String jarPaths;
	protected boolean loadOrigJavaClassPath;
	protected JarClassLoader jcl;

	public void setJarPaths(String jarPaths) {
		this.jarPaths = jarPaths;
	}
	
	public JclClassLoadHelper2() {
		jarPaths = System.getProperty("jarPaths", " ");
	}
	
	@Override
	public void initialize() {
		jcl = new JarClassLoader();
		String[] paths = jarPaths.split(",");
		for (String path : paths) {
			if (!path.trim().equals("")) {
				logger.debug("Adding JarClassLoader from 'user jarPaths': {}", path);
				jcl.add(path);
			}
		}
		
		// Load classpath from originally java.class.path sys prop.
		loadOrigJavaClassPath = Boolean.parseBoolean(System.getProperty("loadOrigJavaClassPath", "false"));
		if (loadOrigJavaClassPath) {
			String javaClassPaths = System.getProperty("java.class.path");
			String sep = System.getProperty("path.separator");
			for (String path : javaClassPaths.split(sep)) {
				if (!path.trim().equals("")) {
					logger.debug("Adding JarClassLoader from 'java.class.path': {}", path);
					jcl.add(path);
				}
			}
		}

		// Call super initialize
		super.initialize();
	}

	@Override
	public ClassLoader getClassLoader() {
		logger.debug("getClassLoader: {}", jcl);
		return jcl;
	}

	@Override
	public URL getResource(String name) {
		logger.debug("getResource: {}", name);
		URL ret = jcl.getResource(name);
		if (ret == null) {
			logger.debug("No found. use parent classLoadHelper.");
			ret = super.getResource(name);
		}
		return ret;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		logger.debug("getResourceAsStream: {}", name);
		InputStream ret = jcl.getResourceAsStream(name);
		if (ret == null) {
			logger.debug("No found. use parent classLoadHelper.");
			ret = super.getResourceAsStream(name);
		}
		return ret;
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		logger.debug("loadClass: {}", className);
		Class<?> ret = null;
		try {
			ret = jcl.loadClass(className);
		} catch (ClassNotFoundException e) {
			logger.debug("No found. use parent classLoadHelper.");
			ret = super.loadClass(className);
		}
		return ret;
	}

}

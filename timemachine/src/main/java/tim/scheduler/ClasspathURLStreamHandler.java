package tim.scheduler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom URL protocol handler to load any resource files using 'classpath' prefix.
 * 
 * Usage: 
 * <code>
 * URL url = new URL(null, "classpath:atest/config.properties", new ClasspathURLStreamHandler());
 * InputStream inStream = url.openConnection().getInputStream();
 * Properties props = new Properties();
 * props.load(inStream);
 * inStream.close();
 * </code>
 * 
 * <p>
 * There are other ways to register 'classpath' as official URL protocol, so that you can simply use the String
 * constructor. The instructions required by JDK seems to be too invasive though. See  
 * java.net.URL(java.lang.String, java.lang.String, int, java.lang.String) javadoc for more.
 * 
 * @author Zemian Deng
 */
public class ClasspathURLStreamHandler extends URLStreamHandler {
		
	private static final String CLASSPATH_PROTOCOL = "classpath";
	private static final Logger logger = LoggerFactory.getLogger(ClasspathURLStreamHandler.class);
	
	@Override
	protected URLConnection openConnection(URL url) throws IOException
	{
		String pro = url.getProtocol();
		if (CLASSPATH_PROTOCOL.equals(pro)) {
			String path = url.getPath();
			logger.info("Loading classpath resource: {}", path);
			URL resUrl = getClassLoader().getResource(path);
			return resUrl.openConnection();
		} else {
			// Use JDK default
			return url.openConnection();
		}
	}

	protected ClassLoader getClassLoader()
	{
		return Thread.currentThread().getContextClassLoader();
	}
}

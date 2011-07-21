package quartz.experiment.spring;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/** 
 * Tiny little Spring Server that takes any xml config file.
 *
 * @author Zemian Deng
 */
public class SpringServer {

	public static void main(String[] args) throws Exception {
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(args);
		ctx.registerShutdownHook();
		boolean wait = Boolean.valueOf(System.getProperty("wait", "true"));
		while (wait)
			Thread.sleep(Long.MAX_VALUE);
	}

}

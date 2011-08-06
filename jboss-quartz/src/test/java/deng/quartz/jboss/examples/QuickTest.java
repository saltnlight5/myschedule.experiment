package deng.quartz.jboss.examples;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTest {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testJava() throws Exception {
		logger.info("hello");
	}
	
}

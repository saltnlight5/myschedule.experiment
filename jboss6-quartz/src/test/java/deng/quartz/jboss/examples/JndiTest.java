package deng.quartz.jboss.examples;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JndiTest {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testListJndi() throws Exception {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			String contextName = "/";
			contextName = "java:";
//			contextName = "jmx";
//			contextName = "java:jmx";
//			contextName = "java:queue";
			
			NamingEnumeration<NameClassPair> nameList = ctx.list(contextName);
			while(nameList.hasMore()) {
				NameClassPair name = nameList.next();
				logger.info("{}", name);
			}
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					logger.error("Failed to close JNDI context.", e);
				}
			}
		}
	}
	
	@Test
	public void testJndi() throws Exception {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			logger.info("JNDI ctx " + ctx);
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("java:/ConnectionFactory");
			logger.info("JNDI ConnectionFactory " + cf);
			
			Destination jmsDest = (Destination)ctx.lookup("testQueue");
			logger.info("Got JMS destination: " + jmsDest);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					logger.error("Failed to close JNDI context.", e);
				}
			}
		}
	}
}

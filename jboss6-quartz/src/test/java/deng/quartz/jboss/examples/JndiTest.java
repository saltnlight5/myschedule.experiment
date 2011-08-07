package deng.quartz.jboss.examples;

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
			contextName = "java:queue";
						
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
	
	/**
	 * In JBoss6, there are 3 types of DataSource:
	 * 1) Local TX DS, 2) No TX DS, 3) XA
	 * 
	 * (For MySQL 1) and 3), you would need to set: com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)
	 * 
	 * You can NOT access DataSource from remote client! You may access them within the server. Try deploy a groovy.war
	 * and test it out like this:
props = new Properties()
props.setProperty('java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory')
props.setProperty('java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces')
def ctx = new javax.naming.InitialContext(props)

quartz18LocalTxDs = ctx.lookup("java:datasource/Quartz18LocalTxDs")
println("quartz18LocalTxDs: " + quartz18LocalTxDs)
conn = quartz18LocalTxDs.getConnection()
println("quartz18LocalTxDs conn: " + conn)
conn.close()

quartz18NonXaDs = ctx.lookup("java:datasource/Quartz18NonXaDs")
println("quartz18NonXaDs: " + quartz18NonXaDs)
conn = quartz18NonXaDs.getConnection()
println("quartz18NonXaDs conn: " + conn)
conn.close()

quartz18NonXaDs = ctx.lookup("java:datasource/Quartz18XaDs")
println("quartz18NonXaDs: " + quartz18NonXaDs)
conn = quartz18NonXaDs.getConnection()
println("quartz18NonXaDs conn: " + conn)
conn.close()
	 */
	@Test
	public void testJndi() throws Exception {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			logger.info("InitialContext: {}", ctx);
			
			Object connFact = ctx.lookup("/ConnectionFactory");
			logger.info("ConnectionFactory: {}", connFact);
									
			Object quartzQueue = ctx.lookup("/queue/QuartzQueue");
			logger.info("quartzQueue: {}", quartzQueue);

			Object quartz18NonXA = ctx.lookup("/datasource/Quartz18NonXA");
			logger.info("quartz18NonXA: {}", quartz18NonXA);
			
			Object quartz18XA = ctx.lookup("/datasource/Quartz18XA");
			logger.info("quartz18XA: {}", quartz18XA);
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

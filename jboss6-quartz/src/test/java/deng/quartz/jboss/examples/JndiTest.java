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
	 * When creating MySQL DataSource in JBoss AS 6 server, remember to deploy the jdbc jar!
	 * 
	 * In JBoss6, there are 3 types of DataSource:
	 * 1) Local TX DS, 2) No TX DS, 3) XA
	 * 
	 * http://community.jboss.org/wiki/ConfigDataSources
	 * 1) Local TX DS - A DS that doesn't support two phase commit
	 * 2) Not TX DS - A DS doesn't participate in JTA
	 * 3) XA - A DS that participate in JTA
	 * 
	 * (For MySQL XA, you would need to set: com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)
	 * 
	 * NOTE: In JBoss6 admin-console, when setup DataSource, it won't save username/password. You must edit the xml
	 * file and add them in manually.
	 * 
	 * You can NOT access DataSource from remote client! You may only access them within the server. Try deploy a groovy.war
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
	 * 
	 * 
	 * You may also use Groovy Sql to verify the quartz table directly without the use of JBoss DataSource:

import groovy.sql.Sql
def sql = Sql.newInstance('jdbc:mysql://localhost/quartz18', 'quartz18', 'quartz18123', 'com.mysql.jdbc.Driver')
sql.eachRow('SELECT * FROM QRTZ_JOB_DETAILS'){ row-> println(row) }

	 */
	@Test
	public void testJndi() throws Exception {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			logger.info("InitialContext: {}", ctx);
			
			Object connFact = ctx.lookup("/ConnectionFactory");
			logger.info("ConnectionFactory: {}", connFact);
									
			Object quartzQueue = ctx.lookup("java:/queue/QuartzQueue");
			logger.info("quartzQueue: {}", quartzQueue);

			Object quartzTopic = ctx.lookup("java:/queue/QuartzTopic");
			logger.info("quartzTopic: {}", quartzTopic);
			
			// This will not work, unless you run it inside the JBoss server instance.!
//			Object quartz18LocalTxDs = ctx.lookup("/datasource/Quartz18LocalTxDs");
//			logger.info("quartz18LocalTxDs: {}", quartz18LocalTxDs);
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

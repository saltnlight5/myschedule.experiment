package deng.quartz.jboss.examples;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class SendToQueue {

	private static Logger logger = Logger.getLogger(JndiTest.class);
	
	public static void main(String[] args) throws Exception {
		String queueName = "/queue/ExampleQueue";
		if (args.length >= 1) {
			queueName = args[0];
		}
		String text = "Just a test msg";
		if (args.length >= 2) {
			text = args[1];
		}
				
		Context ctx = null;
		Connection conn = null;
		
		try {
			ctx = new InitialContext();
						
			logger.trace("JNDI ctx " + ctx);
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			Destination jmsDest = (Destination)ctx.lookup(queueName);
						
			conn = cf.createConnection();
			logger.trace("Established JMS conn " + conn);
			
			logger.trace("Sending a text message to " + queueName);
			logger.trace("Text: " + text);
			Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TextMessage txtMsg = sess.createTextMessage(text);
			MessageProducer producer = sess.createProducer(jmsDest);
			producer.send(txtMsg);
			logger.trace("Message sent.");
			
			producer.close();
			sess.close();
			
			logger.trace("Done.");
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception e) {
					logger.error("Failed to close JNDI context.", e);
				}
			}			
			
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.error("Failed to close JMS connection.", e);
				}
			}
		}
	}
}

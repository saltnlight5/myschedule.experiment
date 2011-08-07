package deng.quartz.jboss.examples;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.quartz.JobDataMap;
import org.quartz.jobs.ee.jms.JmsMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Simple impl that creates JMS text message for quartz demo. */
public class SimpleJmsMessageFactory implements JmsMessageFactory {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Message createMessage(JobDataMap jobDataMap, Session session) {
		try {
			TextMessage msg = session.createTextMessage("This is a quartz JMS message test.");
			logger.info("Message created: {}", msg);
		return msg;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

package myschedule.core.standard;

import org.apache.commons.lang.StringUtils;

import myschedule.core.SchedulerException;

public class Utils {
	public static void assertNotNull(Object obj, String failedMsg) {
		if (obj == null)
			throw new SchedulerException(failedMsg);
	}

	public static void assertNotEmpty(String str, String failedMsg) {
		if (StringUtils.isEmpty(str))
			throw new SchedulerException(failedMsg);
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new SchedulerException("Failed to sleep full " + millis + " ms.", e);
		}
	}
}

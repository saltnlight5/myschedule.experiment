package myschedule.core;

/**
 * 
 * @author Zemian Deng
 */
public class SchedulerException extends RuntimeException {

	/** serialVersionUID - long */
	private static final long serialVersionUID = 1L;

	public SchedulerException() {
	}

	public SchedulerException(String message) {
		super(message);
	}

	public SchedulerException(Throwable cause) {
		super(cause);
	}

	public SchedulerException(String message, Throwable cause) {
		super(message, cause);
	}

}

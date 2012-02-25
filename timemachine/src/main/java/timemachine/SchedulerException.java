package timemachine;

public class SchedulerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SchedulerException() {
		super();
	}

	public SchedulerException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchedulerException(String message) {
		super(message);
	}

	public SchedulerException(Throwable cause) {
		super(cause);
	}
}

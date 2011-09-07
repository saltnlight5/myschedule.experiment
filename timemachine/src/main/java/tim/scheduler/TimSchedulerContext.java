package tim.scheduler;

public class TimSchedulerContext implements SchedulerContext {
	
	protected TimScheduler timScheduler;
	
	public TimSchedulerContext(TimScheduler timScheduler) {
		this.timScheduler = timScheduler;
	}
	
	@Override
	public Scheduler getScheduler() {
		return timScheduler;
	}

}

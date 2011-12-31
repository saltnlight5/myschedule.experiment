package deng.timemachine;

public class SchedulerContextImpl implements SchedulerContext {
	
	protected SchedulerImpl timScheduler;
	
	public SchedulerContextImpl(SchedulerImpl timScheduler) {
		this.timScheduler = timScheduler;
	}
	
	@Override
	public Scheduler getScheduler() {
		return timScheduler;
	}

}

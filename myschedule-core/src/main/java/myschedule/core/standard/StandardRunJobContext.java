package myschedule.core.standard;

import myschedule.core.JobSchedule;
import myschedule.core.RunJobContext;

/** 
 * StandardRunJobContext
 *
 * @author Zemian Deng
 */
public class StandardRunJobContext implements RunJobContext {
	private JobSchedule jobSchedule;
	public StandardRunJobContext(JobSchedule jobSchedule) {
		this.jobSchedule = jobSchedule;
	}
	public JobSchedule getJobSchedule() {
		return jobSchedule;
	}
}

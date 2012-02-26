package timemachine;

import java.util.Date;

import org.junit.Test;

public class FixedIntervalScheduleTest {
	@Test
	public void testDefault() throws Exception {
		FixedIntervalSchedule schedule = new FixedIntervalSchedule();
		Date after = schedule.getNextRun();
		for (int i=0; i<20; i++) {
			System.out.println(i + ": " + after);
			after = schedule.computeNextRun(after);
		}
	}
	
	@Test
	public void testSecondly() throws Exception {
		FixedIntervalSchedule schedule = new FixedIntervalSchedule(2, ScheduleUnit.SECONDS);
		Date after = schedule.getNextRun();
		for (int i=0; i<20; i++) {
			System.out.println(i + ": " + after);
			after = schedule.computeNextRun(after);
		}
	}
}

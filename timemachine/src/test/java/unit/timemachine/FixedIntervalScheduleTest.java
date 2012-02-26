package unit.timemachine;

import java.util.Date;

import org.junit.Test;

import timemachine.FixedIntervalSchedule;
import timemachine.ScheduleUnit;

public class FixedIntervalScheduleTest {
	@Test
	public void testDefault() throws Exception {
		FixedIntervalSchedule schedule = new FixedIntervalSchedule();
		for (Date date : schedule.computeNexRunDates(new Date(), 20)) {
			System.out.println(date);
		}
	}
	
	@Test
	public void testSecondly() throws Exception {
		for (int i = 1; i <= 5; i++) {
			System.out.println(i + " seconds interval.");
			FixedIntervalSchedule schedule = new FixedIntervalSchedule(i, ScheduleUnit.SECONDS);
			for (Date date : schedule.computeNexRunDates(new Date(), 20)) {
				System.out.println(date);
			}
		}
	}
}

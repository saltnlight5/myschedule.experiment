package unit.timemachine.scheduler.schedule;

import java.util.Date;

import org.junit.Test;

import timemachine.scheduler.ScheduleUnit;
import timemachine.scheduler.Schedules;
import timemachine.scheduler.schedule.FixedIntervalSchedule;

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
			FixedIntervalSchedule schedule = new FixedIntervalSchedule(i, ScheduleUnit.SECOND, new Date());
			for (Date date : schedule.computeNexRunDates(new Date(), 20)) {
				System.out.println(date);
			}
		}
	}
	
	@Test
	public void testSchedules() throws Exception {
		System.out.println("Secondly");
		for (Date date : Schedules.secondlySchedule(new Date()).computeNexRunDates(new Date(), 20)) {
			System.out.println(date);
		}
		System.out.println("Minutely");
		for (Date date : Schedules.minutelySchedule(new Date()).computeNexRunDates(new Date(), 20)) {
			System.out.println(date);
		}
		System.out.println("Hourly");
		for (Date date : Schedules.hourlySchedule(new Date()).computeNexRunDates(new Date(), 20)) {
			System.out.println(date);
		}
		System.out.println("Daily");
		for (Date date : Schedules.dailySchedule(new Date()).computeNexRunDates(new Date(), 20)) {
			System.out.println(date);
		}
		
		//TODO: the first runTime is incorrect???
		System.out.println("Weekly");
		for (Date date : Schedules.weeklySchedule(new Date()).computeNexRunDates(new Date(), 20)) {
			System.out.println(date);
		}
	}
}

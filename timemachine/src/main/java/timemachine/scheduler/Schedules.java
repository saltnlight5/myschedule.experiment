package timemachine.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timemachine.scheduler.schedule.FixedIntervalSchedule;

public class Schedules {
	public static final long MILLIS_IN_SECOND = 1000L;
	public static final long MILLIS_IN_MINUTE = 1000L * 60;
	public static final long MILLIS_IN_HOUR = 1000L * 60 * 60;
	public static final long MILLIS_IN_DAY = 1000L * 60 * 60 * 24;
	public static final long MILLIS_IN_WEEK = 1000L* 60 * 60 * 24 * 7;

	public static final String DF_TIME = "HH:mm:ss"; // H (0-23)
	public static final String DF_DATE = "MM/dd/yyyy";
	public static final String DF_DATETIME = "MM/dd/yyyy HH:mm:ss";

	public static long millis() {
		return System.currentTimeMillis();
	}
	public static Date now() {
		return new Date();
	}
	public static Date epoch() {
		return new Date(0);
	}
	
	public static Date time(String date) {
		return toDate(date, DF_TIME);
	}
	public static Date date(String date) {
		return toDate(date, DF_DATE);
	}
	public static Date datetime(String date) {
		return toDate(date, DF_DATETIME);
	}
	public static Date toDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Failed to parse date string: " + date + " in format " + format, e);
		}
	}
	
	public static Schedule secondlySchedule(Date startTime) {
		return new FixedIntervalSchedule(1, ScheduleUnit.SECOND, startTime);
	}
	public static Schedule minutelySchedule(Date startTime) {
		return new FixedIntervalSchedule(1, ScheduleUnit.MINUTE, startTime);
	}
	public static Schedule hourlySchedule(Date startTime) {
		return new FixedIntervalSchedule(1, ScheduleUnit.HOUR, startTime);
	}
	public static Schedule dailySchedule(Date startTime) {
		return new FixedIntervalSchedule(1, ScheduleUnit.DAY, startTime);
	}
	public static Schedule weeklySchedule(Date startTime) {
		return new FixedIntervalSchedule(1, ScheduleUnit.WEEK, startTime);
	}
	public static Schedule repeatSchedule(int count, int interval, Date startTime) {
		FixedIntervalSchedule result = new FixedIntervalSchedule(interval, ScheduleUnit.SECOND, startTime);
		result.endCount(count);
		return result;
	}
}

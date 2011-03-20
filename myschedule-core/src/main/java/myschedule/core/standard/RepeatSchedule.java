package myschedule.core.standard;

import java.util.Date;

import myschedule.core.Schedule;

public class RepeatSchedule implements Schedule {
	private String name;
	private long interval;
	
	public RepeatSchedule(String name, long interval) {
		this.interval = interval;
	}
	
	@Override
	public String toString() {
		return "RepeatSchedule[name=" + name + ", interval=" + interval + "]";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getNextRunJobTime() {
		//TODO: Fix me.
		return new Date(System.currentTimeMillis() + interval);
	}
}

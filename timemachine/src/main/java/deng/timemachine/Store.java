package deng.timemachine;

import java.util.Collection;

public interface Store {

	void addSchedule(Schedule schedule);

	Collection<Schedule> getSchedules();

	void removeSchedule(Schedule schedule);

}

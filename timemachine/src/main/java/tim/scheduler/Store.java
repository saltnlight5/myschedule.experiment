package tim.scheduler;

import java.util.Collection;

public interface Store {

	void addSchedule(Schedule schedule);

	Collection<Schedule> getSchedules();

}

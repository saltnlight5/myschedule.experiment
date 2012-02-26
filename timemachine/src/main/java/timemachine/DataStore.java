package timemachine;

import java.util.Date;
import java.util.List;

public interface DataStore {
	public void storeData(Data data);
	public Data getData(Long id, Class<? extends Data> dataType);
	public List<Data> getAllData(Class<? extends Data> dataType);
	public int getDataCount(Class<? extends Data> dataType);
	public Data deleteData(Long id, Class<? extends Data> dataType);
	
	public List<Schedule> getSchedulesToRun(int maxCount, Date noLaterThan);
	public Date getEarliestRunTime();
}

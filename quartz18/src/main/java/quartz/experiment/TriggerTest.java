package quartz.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * Comment for TriggerTest.
 *
 * @author Zemian Deng
 */
public class TriggerTest
{
	/** 
	 * The Higher the number on priority, the first to get execution thread.
	 */
	@Test
	public void testPriorityOrder()
	{
		Trigger t1 = new SimpleTrigger("t1");
		t1.setPriority(5);
		Trigger t2 = new SimpleTrigger("t2");
		t2.setPriority(6);
		List<Trigger> list = new ArrayList<Trigger>();
		list.add(t1);
		list.add(t2);
		assertThat(list.get(0).getName(), is("t1"));
		assertThat(list.get(1).getName(), is("t2"));
		
		// Mimic what's in the comparator in RAMJobStore.
		Collections.sort(list, new Comparator<Trigger>()
		{

			@Override
			public int compare(Trigger trig1, Trigger trig2)
			{
				int comp = trig2.getPriority() - trig1.getPriority();
		        return comp;
			}
			
		});
		assertThat(list.get(0).getName(), is("t2"));
		assertThat(list.get(1).getName(), is("t1"));
	}
}

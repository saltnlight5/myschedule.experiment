/*
 * Copyright (c) 2007-2011  The Bank of New York Mellon, All Rights Reserved.
 */

package quartz.experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Trigger;

/**
 * Comment for TriggerHelper.
 *
 * @author Zemian Deng
 */
public class TriggerHelper
{
	public static List<Date> getNextFireTimes(Trigger trigger, int maxCount) {
		List<Date> result = new ArrayList<Date>(maxCount);
		Date nextDate = new Date();
		int count = 0;
		while (count++ < maxCount) {
			nextDate = trigger.getFireTimeAfter(nextDate);
			result.add(nextDate);
		}
		return result;
	}
}

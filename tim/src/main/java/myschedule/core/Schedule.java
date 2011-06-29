package myschedule.core;

import java.util.Date;

/**
 * 
 * @author Zemian Deng
 */
public interface Schedule {
	String getName();
	Date getNextRunJobTime();
}

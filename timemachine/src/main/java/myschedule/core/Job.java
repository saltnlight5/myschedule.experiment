package myschedule.core;

/**
 * 
 * @author Zemian Deng
 */
public interface Job {
	void execute(RunJobContext runJobContext);
}

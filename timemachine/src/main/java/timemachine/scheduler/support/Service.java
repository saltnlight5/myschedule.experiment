package timemachine.scheduler.support;

import timemachine.scheduler.service.ServiceContainer;

/**
 * A service that provide init/start/stop/destroy interface.
 *
 * <p>
 * Use {@link ServiceContainer} to auto manage the lifecycles.
 * 
 * @author Zemian Deng
 */
public interface Service {
	void init();	
	void start();	
	void stop();
	void destroy();
	boolean isInited();
	boolean isStarted();
}

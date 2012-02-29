package timemachine.scheduler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timemachine.scheduler.support.AbstractService;
import timemachine.scheduler.support.Service;

/**
 * This container auto manage a list of Service lifecycles as part of it's own lifecyle. The order of lifecycles are:
 * <ul>
 * 	<li>init</li> - Initialize after service instance has been constructed. All setters are called etc.
 * 	<li>start</li> - Start the service
 * 	<li>stop</li> - Stop the service
 * 	<li>destroy</li> Destroy the service
 * </ul> 
 * 
 * <p>We will init and start services in the order it is added, but will stop and destroy in reverse order.
 * 
 * @author Zemian Deng
 */
public class ServiceContainer extends AbstractService {

	/** Service list that's registered in the application. */
	private List<Service> services = new ArrayList<Service>();
	private boolean ignoreInitAndStartException = false;
	
	public void setIgnoreInitAndStartException(boolean ignoreInitAndStartException) {
		this.ignoreInitAndStartException = ignoreInitAndStartException;
	}
	
	/**
	 * @return Get a read-only list of services.
	 */
	public List<Service> getServices() {
		return Collections.unmodifiableList(services);
	}
	
	/**
	 * User should use this to set list of services that they want to init all together as part of container lifecycle.
	 * <p>Usually this is the list added to container before init is called. 
	 * @param services
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}
	
	/**
	 * User may use this method to add service after the container has been inited or started.
	 * @param service
	 */
	public void addService(Service service) {
		services.add(service);
		if (this.isInited())
			service.init();
		if (this.isStarted())
			service.start();
	}
	
	/**
	 * Initialize all services in the order it was set/added.
	 */
	@Override
	protected void initService() {
		for (Service service : services) {
			int idHashCode = System.identityHashCode(service);
			logger.debug("Initializing service identityHashCode={}.", idHashCode);
			try {
				service.init();
			} catch (RuntimeException e) {
				if (ignoreInitAndStartException) {
					logger.error("Failed to initialize service. But will continue!", e);
				} else {
					throw e;
				}
			}
			logger.info("{} initialized (identityHashCode={}).", service, idHashCode);
		}
	}
	
	/**
	 * Start all services in the order it was set/added.
	 */
	@Override
	protected void startService() {
		for (Service service : services) {
			logger.debug("Starting {}.", service);
			try {
				service.start();
			} catch (RuntimeException e) {
				if (ignoreInitAndStartException) {
					logger.error("Failed to stsart service. But will continue!", e);
				} else {
					throw e;
				}
			}
			logger.info("{} started.", service);
		}
	}
	
	/**
	 * Stop all services in the reverse order it was set/added.
	 */
	@Override
	protected void stopService() {
		// Stopping in reverse order.
		for (int i = services.size() - 1; i >= 0; i--) {
			Service service = services.get(i);
			logger.debug("Stopping {}.", service);
			service.stop();
			logger.info("{} stopped.", service);
		}
	}
	
	/**
	 * Destroy all services in the reverse order it was set/added.
	 */
	@Override
	protected void destroyService() {
		// Destroying in reverse order.
		for (int i = services.size() - 1; i >= 0; i--) {
			Service service = services.get(i);
			logger.debug("Destroying {}.", service);
			service.destroy();
			logger.info("{} destroyed.", service);
		}
	}
	
}

package tim.scheduler;

public interface Service {
	
	void start();
	
	void stop();
	
	void init();
	
	void destroy();
	
	boolean isStarted();
	
	boolean isInit();
}

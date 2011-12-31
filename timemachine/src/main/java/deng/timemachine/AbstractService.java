package deng.timemachine;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractService implements Service {

	protected AtomicBoolean started = new AtomicBoolean(false);
	protected AtomicBoolean inited = new AtomicBoolean(false);

	public void startService() {}
	public void stopService() {}
	public void initService() {}
	public void destroyService() {}
	
	@Override
	public void start() {
		if (!inited.get())
			init();
		
		if (!started.get()) {
			started.set(true);
			startService();
		}
	}

	@Override
	public void stop() {
		if (started.get()) {
			stopService();
			started.set(false);
		}
	}

	@Override
	public void init() {
		if (!inited.get()) {
			initService();
			inited.set(true);
		}
	}

	@Override
	public void destroy() {
		if (started.get())
			stop();		
		
		if (inited.get()) {
			destroyService();
			inited.set(false);
		}
	}

	@Override
	public boolean isStarted() {
		return started.get();
	}

	@Override
	public boolean isInit() {
		return inited.get();
	}

}

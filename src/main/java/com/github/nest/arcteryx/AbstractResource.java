/**
 * 
 */
package com.github.nest.arcteryx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nest.arcteryx.event.IResourceEvent;
import com.github.nest.arcteryx.event.IResourceEventDispatcher;
import com.github.nest.arcteryx.event.IResourceEventDispatchers;
import com.github.nest.arcteryx.event.IResourceEventListener;
import com.github.nest.arcteryx.event.IResourceEventListeners;
import com.github.nest.arcteryx.event.IResourceLifecycleEventDispatcher;
import com.github.nest.arcteryx.event.IResourceLifecycleEventListener;
import com.github.nest.arcteryx.event.ResourceEventDispatchers;
import com.github.nest.arcteryx.event.ResourceEventListeners;
import com.github.nest.arcteryx.event.ResourceLifecycleEvent;
import com.github.nest.arcteryx.event.ResourceLifecycleEvent.EventType;
import com.github.nest.arcteryx.event.ResourceLifecycleEventDispatcher;

/**
 * Abstract resource
 * 
 * @author brad.wu
 */
public abstract class AbstractResource implements IResource {
	private String id = null;
	private IResourceEventListeners eventListeners = null;
	private IResourceEventDispatchers eventDispatchers = null;

	/**
	 * after construct class, a did-construct event will be posted. So add
	 * construct logic in {@linkplain #doConstruct()} for ensure the event
	 * actually fired after constructing.
	 */
	public AbstractResource(String id) {
		this.id = id;
		this.doConstruct();
		this.processLifecycleEvent(EventType.RESOURCE_DID_CONSTRUCT);
	}

	/**
	 * do construct resource.<br/>
	 * default do nothing. override this method to add specific logic.
	 */
	protected void doConstruct() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IResource#getId()
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IResource#destroy()
	 */
	public void destroy() {
		ResourceLifecycleEvent evt = this.processLifecycleEvent(EventType.RESOURCE_SHOULD_DESTROY);
		if (evt.isShouldNot()) {
			// stop destroy
			this.getLogger().warn("Stop destroying resource [%1]", this.getId());
			return;
		}

		this.processLifecycleEvent(EventType.RESOURCE_WILL_DESTROY);
		this.doDestroy();
		this.processLifecycleEvent(EventType.RESOURCE_DID_DESTROY);
	}

	/**
	 * do destroy resource.<br/>
	 * default do nothing. override this method to add specific logic.
	 */
	protected void doDestroy() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IResource#addLifecycleListener(com.github.nest.arcteryx.event.IResourceLifecycleEventListener)
	 */
	public void addLifecycleListener(IResourceLifecycleEventListener listener) {
		this.addListener(listener, IResourceLifecycleEventListener.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IResource#removeLifecycleListener(com.github.nest.arcteryx.event.IResourceLifecycleEventListener)
	 */
	public boolean removeLifecycleListener(IResourceLifecycleEventListener listener) {
		return this.removeListener(listener, IResourceLifecycleEventListener.class);
	}

	/**
	 * create event listeners
	 * 
	 * @return
	 */
	protected IResourceEventListeners createEventListeners() {
		return new ResourceEventListeners();
	}

	/**
	 * get event listeners
	 * 
	 * @return
	 */
	protected IResourceEventListeners getEventListeners() {
		if (this.eventListeners == null) {
			synchronized (this) {
				if (this.eventListeners == null) {
					this.eventListeners = this.createEventListeners();
				}
			}
		}
		return this.eventListeners;
	}

	/**
	 * get event listeners by given listener class
	 * 
	 * @param listenerClass
	 * @return
	 */
	protected <T extends IResourceEventListener> T[] getEventListeners(Class<T> listenerClass) {
		return this.getEventListeners().getListeners(listenerClass);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IResource#addListener(com.github.nest.arcteryx.event.IResourceEventListener,
	 *      java.lang.Class)
	 */
	public <T extends IResourceEventListener> void addListener(T listener, Class<T> listenerClass) {
		this.getEventListeners().addListener(listener, listenerClass);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IResource#removeListener(com.github.nest.arcteryx.event.IResourceEventListener,
	 *      java.lang.Class)
	 */
	public <T extends IResourceEventListener> boolean removeListener(T listener, Class<T> listenerClass) {
		return this.getEventListeners().removeListener(listener, listenerClass);
	}

	/**
	 * create event dispatchers
	 * 
	 * @return
	 */
	protected IResourceEventDispatchers createEventDispatchers() {
		IResourceEventDispatchers dispatchers = new ResourceEventDispatchers();
		registerEventDispatcher(dispatchers);
		return dispatchers;
	}

	/**
	 * register event dispatcher
	 * 
	 * @param dispatchers
	 */
	protected void registerEventDispatcher(IResourceEventDispatchers dispatchers) {
		dispatchers.registerDispatcher(new ResourceLifecycleEventDispatcher(), IResourceLifecycleEventDispatcher.class);
	}

	/**
	 * get event dispatchers
	 * 
	 * @return
	 */
	protected IResourceEventDispatchers getEventDispatchers() {
		if (this.eventDispatchers == null) {
			synchronized (this) {
				if (this.eventDispatchers == null) {
					this.eventDispatchers = this.createEventDispatchers();
				}
			}
		}
		return this.eventDispatchers;
	}

	/**
	 * get event dispatcher
	 * 
	 * @param dispatcherClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected <D extends IResourceEventDispatcher> D getEventDispatcher(Class<D> dispatcherClass) {
		return this.getEventDispatchers().getDispatcher(dispatcherClass);
	}

	/**
	 * fire event
	 * 
	 * @param evt
	 */
	protected void fireEvent(IResourceEvent evt) {
		if (evt instanceof ResourceLifecycleEvent) {
			this.processLifecycleEvent((ResourceLifecycleEvent) evt);
		} else {
			throw new IllegalArgumentException("Event [" + evt + "] not supported yet.");
		}
	}

	/**
	 * process lifecycle event
	 * 
	 * @param eventType
	 * @return event
	 */
	protected ResourceLifecycleEvent processLifecycleEvent(EventType eventType) {
		ResourceLifecycleEvent evt = new ResourceLifecycleEvent(this, eventType);
		this.processLifecycleEvent(evt);
		return evt;
	}

	/**
	 * process lifecycle event
	 * 
	 * @param evt
	 */
	protected void processLifecycleEvent(ResourceLifecycleEvent evt) {
		this.getLogger().debug("%1 triggerred", evt);
		this.getEventDispatcher(IResourceLifecycleEventDispatcher.class) //
				.dispatch(evt, this.getEventListeners(IResourceLifecycleEventListener.class));
	}

	/**
	 * get logger
	 * 
	 * @return
	 */
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
}

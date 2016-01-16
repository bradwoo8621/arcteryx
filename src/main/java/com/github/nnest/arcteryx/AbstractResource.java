/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nnest.arcteryx.event.IResourceEvent;
import com.github.nnest.arcteryx.event.IResourceEventDispatcher;
import com.github.nnest.arcteryx.event.IResourceEventDispatchers;
import com.github.nnest.arcteryx.event.IResourceEventListener;
import com.github.nnest.arcteryx.event.IResourceEventListeners;
import com.github.nnest.arcteryx.event.IResourceLifecycleEventDispatcher;
import com.github.nnest.arcteryx.event.IResourceLifecycleEventListener;
import com.github.nnest.arcteryx.event.ResourceEventDispatchers;
import com.github.nnest.arcteryx.event.ResourceEventListeners;
import com.github.nnest.arcteryx.event.ResourceLifecycleEvent;
import com.github.nnest.arcteryx.event.ResourceLifecycleEvent.EventType;
import com.github.nnest.arcteryx.event.ResourceLifecycleEventDispatcher;

/**
 * Abstract resource
 * 
 * @author brad.wu
 */
public abstract class AbstractResource implements IResource {
	private String id = null;
	private IContainer container = null;
	private ISystem system = null;
	private IResourceEventListeners eventListeners = null;
	private IResourceEventDispatchers eventDispatchers = null;

	/**
	 * after construct class, a did-construct event will be posted. So add
	 * construct logic in {@linkplain #doConstruct()} for ensure the event
	 * actually fired after constructing.
	 */
	public AbstractResource(String id) {
		this.setId(id);
		this.doConstruct();
		this.processLifecycleEvent(EventType.RESOURCE_DID_CONSTRUCT);
	}

	/**
	 * do construct resource.<br/>
	 * default do nothing. override this method to add specific logic.
	 */
	protected void doConstruct() {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#getId()
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * set id
	 * 
	 * @param id
	 */
	protected void setId(String id) {
		this.assertIdNotEmpty(id);

		this.id = id;
	}

	/**
	 * assert id not empty.</br>
	 * override this method to cancel the not empty check for resource id
	 * 
	 * @param id
	 */
	protected void assertIdNotEmpty(String id) {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("Id of resource cannot be null");
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#getContainer()
	 */
	@Override
	public IContainer getContainer() {
		return this.container;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#setContainer(com.github.nnest.arcteryx.IContainer)
	 */
	@Override
	public void setContainer(IContainer container) {
		this.container = container;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#getSystem()
	 */
	@Override
	public ISystem getSystem() {
		return this.system;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#setSystem(com.github.nnest.arcteryx.ISystem)
	 */
	@Override
	public void setSystem(ISystem system) {
		this.system = system;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#destroy()
	 */
	public void destroy() {
		ResourceLifecycleEvent evt = this.processLifecycleEvent(EventType.RESOURCE_SHOULD_DESTROY);
		if (evt.isShouldNot()) {
			// stop destroy
			this.getLogger().warn("Stop destroying resource [{}]", this.getId());
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
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#addLifecycleListener(com.github.nnest.arcteryx.event.IResourceLifecycleEventListener)
	 */
	public void addLifecycleListener(IResourceLifecycleEventListener listener) {
		this.addListener(listener, IResourceLifecycleEventListener.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#removeLifecycleListener(com.github.nnest.arcteryx.event.IResourceLifecycleEventListener)
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
	 * @see com.github.nnest.arcteryx.IResource#addListener(com.github.nnest.arcteryx.event.IResourceEventListener,
	 *      java.lang.Class)
	 */
	public <T extends IResourceEventListener> void addListener(T listener, Class<T> listenerClass) {
		this.getEventListeners().addListener(listener, listenerClass);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#removeListener(com.github.nnest.arcteryx.event.IResourceEventListener,
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
		this.getLogger().debug("{} triggerred", evt);
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

	/**
	 * original toString from {@linkplain Object#toString()}
	 * 
	 * @return
	 */
	protected String originalToString() {
		return super.toString();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IResource#getPath()
	 */
	@Override
	public String getPath() {
		IContainer container = this.getContainer();
		if (container == null) {
			return this.getId();
		} else {
			List<String> ids = new LinkedList<String>();
			while (container != null) {
				ids.add(0, container.getId());
				container = container.getContainer();
			}
			ids.add(this.getId());
			return StringUtils.join(ids, IResource.SEPARATOR_CHAR);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.originalToString() + " [id=" + this.getPath() + "]";
	}
}

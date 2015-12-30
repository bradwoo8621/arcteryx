/**
 * 
 */
package com.github.nest.arcteryx;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.nest.arcteryx.event.IResourceEvent;
import com.github.nest.arcteryx.event.IResourceEventDispatchers;
import com.github.nest.arcteryx.event.IResourceRegistrationEventDispatcher;
import com.github.nest.arcteryx.event.IResourceRegistrationEventListener;
import com.github.nest.arcteryx.event.ResourceRegistrationEvent;
import com.github.nest.arcteryx.event.ResourceRegistrationEvent.EventType;
import com.google.common.base.Strings;
import com.github.nest.arcteryx.event.ResourceRegistrationEventDispatcher;

/**
 * Abstract container
 * 
 * @author brad.wu
 */
public abstract class AbstractContainer extends AbstractResource implements IContainer {
	private Map<String, IResource> resourceMap = new ConcurrentHashMap<String, IResource>();

	public AbstractContainer(String id) {
		super(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IContainer#findResource(java.lang.String)
	 */
	@Override
	public IResource findResource(String resourceId) throws ResourceNotFoundException {
		IResource resource = this.resourceMap.get(resourceId);
		if (resource == null) {
			throw new ResourceNotFoundException(
					"Resource[" + resourceId + "] not found in container[" + this.getId() + "]");
		}
		return resource;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IContainer#registerResource(com.github.nest.arcteryx.IResource)
	 */
	@Override
	public IResource registerResource(IResource resource) {
		if (resource == null) {
			throw new IllegalArgumentException("Resource cannot be null when registration");
		}

		IResource original = this.unregisterResource(resource.getId());
		this.processRegistrationEvent(resource, EventType.RESOURCE_WILL_REGISTER);
		this.resourceMap.put(resource.getId(), resource);
		this.processRegistrationEvent(resource, EventType.RESOURCE_DID_REGISTER);
		return original;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IContainer#unregisterResource(java.lang.String)
	 */
	@Override
	public IResource unregisterResource(String resourceId) {
		if (Strings.isNullOrEmpty(resourceId)) {
			throw new IllegalArgumentException("Resource ID cannot be null or empty string when unregister");
		}

		IResource origin = this.resourceMap.get(resourceId);
		if (origin != null) {
			ResourceRegistrationEvent evt = this.processRegistrationEvent(origin, EventType.RESOURCE_SHOULD_UNREGISTER);
			if (evt.isShouldNot()) {
				this.getLogger().warn("Stop unregister resource [{}] from component [{}]", resourceId, this.getId());
				// stop unregister, return null
				return null;
			}

			this.processRegistrationEvent(origin, EventType.RESOURCE_WILL_UNREGISTER);
			// maybe origin was changed, not sure
			origin = this.resourceMap.remove(resourceId);
			this.processRegistrationEvent(origin, EventType.RESOURCE_DID_UNREGISTER);
			return origin;
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IContainer#getAllResources()
	 */
	@Override
	public Collection<IResource> getAllResources() {
		return this.resourceMap.values();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IContainer#addResourceRegistrationListener(com.github.nest.arcteryx.event.IResourceRegistrationEventListener)
	 */
	@Override
	public void addResourceRegistrationListener(IResourceRegistrationEventListener listener) {
		this.addListener(listener, IResourceRegistrationEventListener.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IContainer#removeResourceRegistrationListener(com.github.nest.arcteryx.event.IResourceRegistrationEventListener)
	 */
	@Override
	public boolean removeResourceRegistrationListener(IResourceRegistrationEventListener listener) {
		return this.removeListener(listener, IResourceRegistrationEventListener.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.AbstractResource#fireEvent(com.github.nest.arcteryx.event.IResourceEvent)
	 */
	@Override
	protected void fireEvent(IResourceEvent evt) {
		if (evt instanceof ResourceRegistrationEvent) {
			this.processRegistrationEvent((ResourceRegistrationEvent) evt);
		} else {
			super.fireEvent(evt);
		}
	}

	/**
	 * process lifecycle event
	 * 
	 * @param eventType
	 * @return event
	 */
	protected ResourceRegistrationEvent processRegistrationEvent(IResource resource, EventType eventType) {
		ResourceRegistrationEvent evt = new ResourceRegistrationEvent(this, resource, eventType);
		this.processRegistrationEvent(evt);
		return evt;
	}

	/**
	 * process lifecycle event
	 * 
	 * @param evt
	 */
	protected void processRegistrationEvent(ResourceRegistrationEvent evt) {
		this.getLogger().debug("{} triggerred", evt);
		this.getEventDispatcher(IResourceRegistrationEventDispatcher.class) //
				.dispatch(evt, this.getEventListeners(IResourceRegistrationEventListener.class));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.AbstractResource#registerEventDispatcher(com.github.nest.arcteryx.event.IResourceEventDispatchers)
	 */
	@Override
	protected void registerEventDispatcher(IResourceEventDispatchers dispatchers) {
		super.registerEventDispatcher(dispatchers);
		dispatchers.registerDispatcher(new ResourceRegistrationEventDispatcher(),
				IResourceRegistrationEventDispatcher.class);
	}
}

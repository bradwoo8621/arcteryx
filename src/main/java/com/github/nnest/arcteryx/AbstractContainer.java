/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.nnest.arcteryx.event.IResourceEvent;
import com.github.nnest.arcteryx.event.IResourceEventDispatchers;
import com.github.nnest.arcteryx.event.IResourceRegistrationEventDispatcher;
import com.github.nnest.arcteryx.event.IResourceRegistrationEventListener;
import com.github.nnest.arcteryx.event.ResourceRegistrationEvent;
import com.github.nnest.arcteryx.event.ResourceRegistrationEventDispatcher;
import com.github.nnest.arcteryx.event.ResourceRegistrationEvent.EventType;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

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
	 * @see com.github.nnest.arcteryx.IContainer#findResource(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IResource> T findResource(String resourceId) throws ResourceNotFoundException {
		IResource resource = this.resourceMap.get(resourceId);
		if (resource == null) {
			throw new ResourceNotFoundException(
					"Resource[" + resourceId + "] not found in container[" + this.getId() + "]");
		}
		return (T) resource;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IContainer#registerResource(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public IResource registerResource(IResource resource) {
		if (resource == null) {
			throw new IllegalArgumentException("Resource cannot be null when registration");
		}

		if (!this.accepted(resource)) {
			throw new IllegalArgumentException("Resource[" + resource.getId() + "] doesn't accept by container");
		}

		IResource original = this.unregisterResource(resource.getId());
		this.processRegistrationEvent(resource, EventType.RESOURCE_WILL_REGISTER);
		this.resourceMap.put(resource.getId(), resource);
		this.processRegistrationEvent(resource, EventType.RESOURCE_DID_REGISTER);
		return original;
	}

	/**
	 * default return true, always accept any kind of resource
	 * 
	 * @see com.github.nnest.arcteryx.IContainer#accepted(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public boolean accepted(IResource resource) {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IContainer#unregisterResource(java.lang.String)
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
	 * @see com.github.nnest.arcteryx.IContainer#getResources()
	 */
	@Override
	public Collection<IResource> getResources() {
		return this.resourceMap.values();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IContainer#getResources(java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends IResource> Collection<T> getResources(final Class<T> resourceClass) {
		Collection<IResource> all = this.getResources();
		return Collections2.filter(all, new Predicate() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.google.common.base.Predicate#apply(java.lang.Object)
			 */
			@Override
			public boolean apply(Object input) {
				return resourceClass.isInstance(input);
			}
		});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IContainer#addResourceRegistrationListener(com.github.nnest.arcteryx.event.IResourceRegistrationEventListener)
	 */
	@Override
	public void addResourceRegistrationListener(IResourceRegistrationEventListener listener) {
		this.addListener(listener, IResourceRegistrationEventListener.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IContainer#removeResourceRegistrationListener(com.github.nnest.arcteryx.event.IResourceRegistrationEventListener)
	 */
	@Override
	public boolean removeResourceRegistrationListener(IResourceRegistrationEventListener listener) {
		return this.removeListener(listener, IResourceRegistrationEventListener.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.AbstractResource#fireEvent(com.github.nnest.arcteryx.event.IResourceEvent)
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
	 * @see com.github.nnest.arcteryx.AbstractResource#registerEventDispatcher(com.github.nnest.arcteryx.event.IResourceEventDispatchers)
	 */
	@Override
	protected void registerEventDispatcher(IResourceEventDispatchers dispatchers) {
		super.registerEventDispatcher(dispatchers);
		dispatchers.registerDispatcher(new ResourceRegistrationEventDispatcher(),
				IResourceRegistrationEventDispatcher.class);
	}
}

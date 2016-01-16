/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.nnest.arcteryx.event.IResourceEvent;
import com.github.nnest.arcteryx.event.IResourceEventDispatchers;
import com.github.nnest.arcteryx.event.IResourceRegistrationEventDispatcher;
import com.github.nnest.arcteryx.event.IResourceRegistrationEventListener;
import com.github.nnest.arcteryx.event.ResourceRegistrationEvent;
import com.github.nnest.arcteryx.event.ResourceRegistrationEvent.EventType;
import com.github.nnest.arcteryx.event.ResourceRegistrationEventDispatcher;

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
	public <T extends IResource> T findResource(String resourceId) {
		if (StringUtils.isEmpty(resourceId)) {
			throw new IllegalArgumentException("Resource id cannot be null");
		}

		if (resourceId.indexOf(IResource.SEPARATOR_CHAR) != -1) {
			// qualified resource id
			return this.findResource(resourceId.split(IResource.SEPARATOR));
		} else {
			// single resource id
			IResource resource = this.resourceMap.get(resourceId);
			if (resource == null && this.getLogger().isErrorEnabled()) {
				this.getLogger().info("Resource[{}] not found in container [{}]", resourceId, this.getPath());
			}

			return (T) resource;
		}
	}

	/**
	 * find resource by given resource ids
	 * 
	 * @param resourceIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IResource> T findResource(String[] resourceIds) {
		IContainer container = this;
		for (int index = 0, count = resourceIds.length - 1; index <= count; index++) {
			String resourceId = resourceIds[index];
			IResource resource = container.findResource(resourceId);
			if (index == count) {
				return (T) resource;
			} else if (resource == null) {
				if (this.getLogger().isInfoEnabled()) {
					this.getLogger().info("Resource[{}] not found cause by [{}] in container [{}] not found", //
							StringUtils.join(resourceIds, IResource.SEPARATOR), //
							StringUtils.join(ArrayUtils.subarray(resourceIds, 0, index + 1), IResource.SEPARATOR),
							this.getPath());
				}
				return null;
			} else if (resource instanceof IContainer) {
				container = (IContainer) resource;
			} else {
				if (this.getLogger().isInfoEnabled()) {
					this.getLogger().info("Resource[{}] not found cause by [{}] in container [{}] is not a container", //
							StringUtils.join(resourceIds, IResource.SEPARATOR), //
							StringUtils.join(ArrayUtils.subarray(resourceIds, 0, index + 1), IResource.SEPARATOR),
							this.getPath());
				}
				return null;
			}
		}
		return (T) container;
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
		if (StringUtils.isEmpty(resourceId)) {
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
		return CollectionUtils.select(all, new Predicate() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see org.apache.commons.collections4.Predicate#evaluate(java.lang.Object)
			 */
			@Override
			public boolean evaluate(Object object) {
				return resourceClass.isInstance(object);
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

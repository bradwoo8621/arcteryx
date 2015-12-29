/**
 * 
 */
package com.github.nest.arcteryx.event;

import com.github.nest.arcteryx.IContainer;
import com.github.nest.arcteryx.IResource;

/**
 * Resource registration event
 * 
 * @author brad.wu
 */
public class ResourceRegistrationEvent extends AbstractResourceEvent {
	public final static int RESOURCE_WILL_REGISTER = 1;
	public final static int RESOURCE_DID_REGISTER = 2;
	public final static int RESOURCE_SHOULD_UNREGISTER = 3;
	public final static int RESOURCE_WILL_UNREGISTER = 4;
	public final static int RESOURCE_DID_UNREGISTER = 5;

	/**
	 * Resource registration event type
	 * 
	 * @author brad.wu
	 */
	public enum EventType implements IResourceEventType {
		RESOURCE_WILL_REGISTER(ResourceRegistrationEvent.RESOURCE_WILL_REGISTER), //
		RESOURCE_DID_REGISTER(ResourceRegistrationEvent.RESOURCE_DID_REGISTER), //
		RESOURCE_SHOULD_UNREGISTER(ResourceRegistrationEvent.RESOURCE_SHOULD_UNREGISTER), //
		RESOURCE_WILL_UNREGISTER(ResourceRegistrationEvent.RESOURCE_WILL_UNREGISTER), //
		RESOURCE_DID_UNREGISTER(ResourceRegistrationEvent.RESOURCE_DID_UNREGISTER); //

		private int code = 0;

		EventType(int code) {
			this.code = code;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.event.IResourceEventType#getCode()
		 */
		@Override
		public int getCode() {
			return this.code;
		}
	}

	private IResource resource = null;
	private boolean shouldNot = false;

	public ResourceRegistrationEvent(IContainer eventTarget, IResource resource, EventType eventType) {
		super(eventTarget, eventType);
		this.resource = resource;
	}

	/**
	 * get component which fire this event
	 * 
	 * @return
	 */
	public IContainer getContainer() {
		return (IContainer) this.getEventTarget();
	}

	/**
	 * get resource which do registration or unregistration
	 * 
	 * @return
	 */
	public IResource getResource() {
		return this.resource;
	}

	/**
	 * @return the shouldNot
	 */
	public boolean isShouldNot() {
		return shouldNot;
	}

	/**
	 * set true when handle should unregister event to stop unregister
	 * 
	 * @param shouldNot
	 *            the shouldNot to set
	 * @see EventType#RESOURCE_SHOULD_UNREGISTER
	 */
	public void setShouldNot(boolean shouldNot) {
		this.shouldNot = shouldNot;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceRegistrationEvent [container=" + this.getContainer().getId() + ", resource="
				+ this.getResource().getId() + ", type=" + this.getEventType() + "]";
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx.event;

import com.github.nnest.arcteryx.IResource;

/**
 * Resource lifecycle event.
 * 
 * @author brad.wu
 */
public class ResourceLifecycleEvent extends AbstractResourceEvent {
	public final static int RESOURCE_DID_CONSTRUCT = 1;
	public final static int RESOURCE_SHOULD_DESTROY = 2;
	public final static int RESOURCE_WILL_DESTROY = 3;
	public final static int RESOURCE_DID_DESTROY = 4;

	/**
	 * Resource lifecycle event type
	 * 
	 * @author brad.wu
	 */
	public enum EventType implements IResourceEventType {
		RESOURCE_DID_CONSTRUCT(ResourceLifecycleEvent.RESOURCE_DID_CONSTRUCT), //
		RESOURCE_SHOULD_DESTROY(ResourceLifecycleEvent.RESOURCE_SHOULD_DESTROY), //
		RESOURCE_WILL_DESTROY(ResourceLifecycleEvent.RESOURCE_WILL_DESTROY), //
		RESOURCE_DID_DESTROY(ResourceLifecycleEvent.RESOURCE_DID_DESTROY);

		private int code = 0;

		EventType(int code) {
			this.code = code;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.IResourceEventType#getCode()
		 */
		@Override
		public int getCode() {
			return this.code;
		}
	}

	private boolean shouldNot = false;

	public ResourceLifecycleEvent(IResource eventTarget, EventType eventType) {
		super(eventTarget, eventType);
	}

	/**
	 * @return the shouldNot
	 */
	public boolean isShouldNot() {
		return shouldNot;
	}

	/**
	 * set true when handle should destroy event to stop destroy
	 * 
	 * @param shouldNot
	 *            the shouldNot to set
	 * @see EventType#RESOURCE_SHOULD_DESTROY
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
		return "ResourceLifecycleEvent [target=" + getEventTarget().getId() + ", type=" + getEventType() + "]";
	}
}

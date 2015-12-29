/**
 * 
 */
package com.github.nest.arcteryx.event;

import com.github.nest.arcteryx.IResource;

/**
 * Abstract resource event
 * 
 * @author brad.wu
 */
public abstract class AbstractResourceEvent implements IResourceEvent {
	private IResource eventTarget = null;
	private IResourceEventType eventType = null;

	public AbstractResourceEvent(IResource eventTarget, IResourceEventType eventType) {
		this.setEventTarget(eventTarget);
		this.setEventType(eventType);
	}

	/**
	 * set event target
	 * 
	 * @param eventTarget
	 */
	protected void setEventTarget(IResource eventTarget) {
		this.eventTarget = eventTarget;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceEvent#getEventTarget()
	 */
	public IResource getEventTarget() {
		return this.eventTarget;
	}

	/**
	 * set event type
	 * 
	 * @param eventType
	 */
	protected void setEventType(IResourceEventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceEvent#getEventType()
	 */
	public IResourceEventType getEventType() {
		return this.eventType;
	}
}

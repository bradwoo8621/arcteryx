/**
 * 
 */
package com.github.nest.arcteryx.event;

import com.github.nest.arcteryx.IResource;

/**
 * Resource event
 * 
 * @author brad.wu
 */
public interface IResourceEvent {
	/**
	 * get event target resource which fire this event
	 * 
	 * @return
	 */
	IResource getEventTarget();

	/**
	 * get event type
	 * 
	 * @return
	 */
	IResourceEventType getEventType();
}

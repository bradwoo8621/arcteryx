/**
 * 
 */
package com.github.nest.arcteryx.event;

/**
 * Resource lifecycle event adapter
 * 
 * @author brad.wu
 */
public class ResourceLifecycleEventAdapter implements IResourceLifecycleEventListener {
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceLifecycleEventListener#resourceDidConstruct(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceDidConstruct(ResourceLifecycleEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceLifecycleEventListener#resourceShouldDestroy(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceShouldDestroy(ResourceLifecycleEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceLifecycleEventListener#resourceWillDestroy(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceWillDestroy(ResourceLifecycleEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceLifecycleEventListener#resourceDidDestory(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceDidDestory(ResourceLifecycleEvent evt) {
		// default do nothing
	}
}

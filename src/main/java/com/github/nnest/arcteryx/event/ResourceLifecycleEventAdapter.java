/**
 * 
 */
package com.github.nnest.arcteryx.event;

/**
 * Resource lifecycle event adapter
 * 
 * @author brad.wu
 */
public class ResourceLifecycleEventAdapter implements IResourceLifecycleEventListener {
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceLifecycleEventListener#resourceDidConstruct(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceDidConstruct(ResourceLifecycleEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceLifecycleEventListener#resourceShouldDestroy(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceShouldDestroy(ResourceLifecycleEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceLifecycleEventListener#resourceWillDestroy(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceWillDestroy(ResourceLifecycleEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceLifecycleEventListener#resourceDidDestory(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
	 */
	public void resourceDidDestory(ResourceLifecycleEvent evt) {
		// default do nothing
	}
}

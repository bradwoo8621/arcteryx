/**
 * 
 */
package com.github.nest.arcteryx.event;

/**
 * Resource lifecycle event listener
 * 
 * @author brad.wu
 */
public interface IResourceLifecycleEventListener extends IResourceEventListener {
	/**
	 * after resource constructed
	 * 
	 * @param evt
	 */
	void resourceDidConstruct(ResourceLifecycleEvent evt);

	/**
	 * resource should destroy
	 * 
	 * @param evt
	 */
	void resourceShouldDestroy(ResourceLifecycleEvent evt);

	/**
	 * before resource destroy
	 * 
	 * @param evt
	 */
	void resourceWillDestroy(ResourceLifecycleEvent evt);

	/**
	 * after resource destroyed
	 * 
	 * @param evt
	 */
	void resourceDidDestory(ResourceLifecycleEvent evt);
}

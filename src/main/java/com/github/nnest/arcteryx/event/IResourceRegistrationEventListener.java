/**
 * 
 */
package com.github.nnest.arcteryx.event;

/**
 * Component registration event listener
 * 
 * @author brad.wu
 */
public interface IResourceRegistrationEventListener extends IResourceEventListener {
	/**
	 * resource will register
	 * 
	 * @param evt
	 */
	void resourceWillRegister(ResourceRegistrationEvent evt);

	/**
	 * resource did register
	 * 
	 * @param evt
	 */
	void resourceDidRegister(ResourceRegistrationEvent evt);

	/**
	 * resource should unregister
	 * 
	 * @param evt
	 */
	void resourceShouldUnregister(ResourceRegistrationEvent evt);

	/**
	 * resource will unregister
	 * 
	 * @param evt
	 * 
	 */
	void resourceWillUnregister(ResourceRegistrationEvent evt);

	/**
	 * resource did unregister
	 * 
	 * @param evt
	 */
	void resourceDidUnregister(ResourceRegistrationEvent evt);
}

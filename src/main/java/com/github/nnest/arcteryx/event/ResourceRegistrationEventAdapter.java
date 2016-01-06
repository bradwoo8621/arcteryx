/**
 * 
 */
package com.github.nnest.arcteryx.event;

/**
 * Resource registration event adapter
 * 
 * @author brad.wu
 */
public class ResourceRegistrationEventAdapter implements IResourceRegistrationEventListener {
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceRegistrationEventListener#resourceWillRegister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
	 */
	public void resourceWillRegister(ResourceRegistrationEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceRegistrationEventListener#resourceDidRegister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
	 */
	public void resourceDidRegister(ResourceRegistrationEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceRegistrationEventListener#resourceShouldUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
	 */
	public void resourceShouldUnregister(ResourceRegistrationEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceRegistrationEventListener#resourceWillUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
	 */
	public void resourceWillUnregister(ResourceRegistrationEvent evt) {
		// default do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceRegistrationEventListener#resourceDidUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
	 */
	public void resourceDidUnregister(ResourceRegistrationEvent evt) {
		// default do nothing
	}
}

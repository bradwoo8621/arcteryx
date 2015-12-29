/**
 * 
 */
package com.github.nest.arcteryx.event;

/**
 * Resource registration event dispatcher
 * 
 * @author brad.wu
 */
public class ResourceRegistrationEventDispatcher implements IResourceRegistrationEventDispatcher {
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceEventDispatcher#dispatch(com.github.nest.arcteryx.event.IResourceEvent,
	 *      com.github.nest.arcteryx.event.IResourceEventListener[])
	 */
	@Override
	public void dispatch(ResourceRegistrationEvent evt, IResourceRegistrationEventListener... listeners) {
		switch (evt.getEventType().getCode()) {
		case ResourceRegistrationEvent.RESOURCE_WILL_REGISTER:
			for (IResourceRegistrationEventListener listener : listeners) {
				listener.resourceWillRegister(evt);
			}
			break;
		case ResourceRegistrationEvent.RESOURCE_DID_REGISTER:
			for (IResourceRegistrationEventListener listener : listeners) {
				listener.resourceDidRegister(evt);
			}
			break;
		case ResourceRegistrationEvent.RESOURCE_SHOULD_UNREGISTER:
			for (IResourceRegistrationEventListener listener : listeners) {
				listener.resourceShouldUnregister(evt);
			}
			break;
		case ResourceRegistrationEvent.RESOURCE_WILL_UNREGISTER:
			for (IResourceRegistrationEventListener listener : listeners) {
				listener.resourceWillUnregister(evt);
			}
			break;
		case ResourceRegistrationEvent.RESOURCE_DID_UNREGISTER:
			for (IResourceRegistrationEventListener listener : listeners) {
				listener.resourceDidUnregister(evt);
			}
			break;
		default:
			throw new IllegalArgumentException(evt + " is not supported yet");
		}
	}
}

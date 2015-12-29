/**
 * 
 */
package com.github.nest.arcteryx.event;

/**
 * Resource lifecycle event dispatcher
 * 
 * @author brad.wu
 */
public class ResourceLifecycleEventDispatcher implements IResourceLifecycleEventDispatcher {
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.event.IResourceEventDispatcher#dispatch(com.github.nest.arcteryx.event.IResourceEvent,
	 *      com.github.nest.arcteryx.event.IResourceEventListener)
	 */
	@Override
	public void dispatch(ResourceLifecycleEvent evt, IResourceLifecycleEventListener... listeners) {
		switch (evt.getEventType().getCode()) {
		case ResourceLifecycleEvent.RESOURCE_DID_CONSTRUCT:
			for (IResourceLifecycleEventListener listener : listeners) {
				listener.resourceDidConstruct(evt);
			}
			break;
		case ResourceLifecycleEvent.RESOURCE_SHOULD_DESTROY:
			for (IResourceLifecycleEventListener listener : listeners) {
				listener.resourceShouldDestroy(evt);
			}
			break;
		case ResourceLifecycleEvent.RESOURCE_WILL_DESTROY:
			for (IResourceLifecycleEventListener listener : listeners) {
				listener.resourceWillDestroy(evt);
			}
			break;
		case ResourceLifecycleEvent.RESOURCE_DID_DESTROY:
			for (IResourceLifecycleEventListener listener : listeners) {
				listener.resourceDidDestory(evt);
			}
			break;
		default:
			throw new IllegalArgumentException(evt + "] is not supported yet");
		}
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resource event dispatchers
 * 
 * @author brad.wu
 */
public class ResourceEventDispatchers implements IResourceEventDispatchers {
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends IResourceEventDispatcher>, IResourceEventDispatcher> map = new ConcurrentHashMap<Class<? extends IResourceEventDispatcher>, IResourceEventDispatcher>();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceEventDispatchers#getDispatcher(java.lang.Class)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <D extends IResourceEventDispatcher> D getDispatcher(Class<D> dispatcherClass) {
		return (D) map.get(dispatcherClass);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceEventDispatchers#registerDispatcher(com.github.nnest.arcteryx.event.IResourceEventDispatcher,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void registerDispatcher(IResourceEventDispatcher dispatcher,
			Class<? extends IResourceEventDispatcher> dispatcherClass) {
		map.put(dispatcherClass, dispatcher);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceEventDispatchers#unregisterDispatcher(java.lang.Class)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <D extends IResourceEventDispatcher> D unregisterDispatcher(Class<D> dispatcherClass) {
		return (D) map.remove(dispatcherClass);
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx.event;

/**
 * Resource event dispatchers
 * 
 * @author brad.wu
 */
public interface IResourceEventDispatchers {
	/**
	 * get dispatcher by given dispatcher class
	 * 
	 * @param dispatcherClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	<D extends IResourceEventDispatcher> D getDispatcher(Class<D> dispatcherClass);

	/**
	 * register dispatcher
	 * 
	 * @param dispatcher
	 * @param dispatcherClass
	 */
	@SuppressWarnings("rawtypes")
	void registerDispatcher(IResourceEventDispatcher dispatcher,
			Class<? extends IResourceEventDispatcher> dispatcherClass);

	/**
	 * unregister dispatcher
	 * 
	 * @param dispatcherClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	<D extends IResourceEventDispatcher> D unregisterDispatcher(Class<D> dispatcherClass);
}

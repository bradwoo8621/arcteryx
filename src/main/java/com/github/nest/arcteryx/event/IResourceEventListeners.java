/**
 * 
 */
package com.github.nest.arcteryx.event;

/**
 * Resource event support.
 * 
 * @author brad.wu
 */
public interface IResourceEventListeners {
	/**
	 * add event listener
	 * 
	 * @param listener
	 * @param listenerClass
	 */
	<T extends IResourceEventListener> void addListener(T listener, Class<T> listenerClass);

	/**
	 * remove event listener
	 * 
	 * @param listener
	 * @param listenerClass
	 * @return true when remove successfully
	 */
	<T extends IResourceEventListener> boolean removeListener(T listener, Class<T> listenerClass);

	/**
	 * get listeners by given class
	 * 
	 * @param listenerClass
	 * @return
	 */
	<T extends IResourceEventListener> T[] getListeners(Class<T> listenerClass);
}

/**
 * 
 */
package com.github.nest.arcteryx;

import com.github.nest.arcteryx.event.IResourceEventListener;
import com.github.nest.arcteryx.event.IResourceLifecycleEventListener;

/**
 * Resource. <br/>
 * Everything is a resource.
 * 
 * @author brad.wu
 */
public interface IResource {
	/**
	 * get identity
	 * 
	 * @return
	 */
	String getId();

	/**
	 * get container
	 * 
	 * @return
	 */
	IContainer getContainer();

	/**
	 * set container
	 * 
	 * @param container
	 */
	void setContainer(IContainer container);

	/**
	 * destroy self
	 */
	void destroy();

	/**
	 * add lifecycle listener
	 * 
	 * @param listener
	 */
	void addLifecycleListener(IResourceLifecycleEventListener listener);

	/**
	 * remove lifecycle listener
	 * 
	 * @param listener
	 * @return true when remove successfully
	 */
	boolean removeLifecycleListener(IResourceLifecycleEventListener listener);

	/**
	 * add listener
	 * 
	 * @param listener
	 * @param listenerClass
	 */
	<T extends IResourceEventListener> boolean removeListener(T listener, Class<T> listenerClass);

	/**
	 * remove listener
	 * 
	 * @param listener
	 * @param listenerClass
	 * @return
	 */
	<T extends IResourceEventListener> void addListener(T listener, Class<T> listenerClass);
}

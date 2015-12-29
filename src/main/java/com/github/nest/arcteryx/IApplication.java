/**
 * 
 */
package com.github.nest.arcteryx;

import java.util.Collection;

/**
 * Application.
 * 
 * @author brad.wu
 */
public interface IApplication extends IContainer {
	/**
	 * find component by given component id
	 * 
	 * @param componentId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	IComponent findComponent(String componentId) throws ResourceNotFoundException;

	/**
	 * register component
	 * 
	 * @param component
	 */
	IComponent registerComponent(IComponent component);

	/**
	 * unregister component by given component id
	 * 
	 * @param componentId
	 * @return
	 */
	IComponent unregisterComponent(String componentId);

	/**
	 * get all components of current application
	 * 
	 * @return
	 */
	Collection<IComponent> getAllComponents();
}

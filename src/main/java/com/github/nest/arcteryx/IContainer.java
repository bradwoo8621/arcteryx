/**
 * 
 */
package com.github.nest.arcteryx;

import java.util.Collection;

import com.github.nest.arcteryx.event.IResourceRegistrationEventListener;

/**
 * Resource container
 * 
 * @author brad.wu
 */
public interface IContainer extends IResource {
	/**
	 * find resource by given resource id
	 * 
	 * @param resourceId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	IResource findResource(String resourceId) throws ResourceNotFoundException;

	/**
	 * register resource
	 * 
	 * @param resource
	 * @return the original resource in component which has same id with given
	 *         resource, if existed. or null.
	 */
	IResource registerResource(IResource resource);

	/**
	 * unregister resource by given resource id
	 * 
	 * @param resourceId
	 * @return
	 */
	IResource unregisterResource(String resourceId);

	/**
	 * get all resources of current component
	 * 
	 * @return
	 */
	Collection<IResource> getAllResources();

	/**
	 * add resource registration listener
	 * 
	 * @param listener
	 */
	void addResourceRegistrationListener(IResourceRegistrationEventListener listener);

	/**
	 * remove resource registration listener
	 * 
	 * @param listener
	 * @return
	 */
	boolean removeResourceRegistrationListener(IResourceRegistrationEventListener listener);
}

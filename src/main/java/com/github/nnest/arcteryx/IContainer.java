/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;

import com.github.nnest.arcteryx.event.IResourceRegistrationEventListener;

/**
 * Resource container
 * 
 * @author brad.wu
 */
public interface IContainer extends IResource {
	/**
	 * find resource by given resource id
	 * 
	 * @param relativePath
	 * @return null if not found
	 */
	<T extends IResource> T findResource(String relativePath);

	/**
	 * find resource by given resource ids segments
	 * 
	 * @param relativePath
	 * @return null if not found
	 */
	<T extends IResource> T findResource(String[] relativePath);

	/**
	 * register resource
	 * 
	 * @param resource
	 * @return the original resource in component which has same id with given
	 *         resource, if existed. or null.
	 */
	IResource registerResource(IResource resource);

	/**
	 * check the resource can be accepted as a child of current container or not
	 * 
	 * @param resource
	 * @return
	 */
	boolean accepted(IResource resource);

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
	Collection<IResource> getResources();

	/**
	 * get specific resources by given resource class. resources which
	 * implements the appointed class will be returned.
	 * 
	 * @param resourceClass
	 * @return
	 */
	<T extends IResource> Collection<T> getResources(Class<T> resourceClass);

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

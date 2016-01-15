/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;

/**
 * Enterprise.</br>
 * Enterprise contains system.
 * 
 * @author brad.wu
 * @see ISystem
 * @see IApplication
 * @see IComponent
 * @see IResource
 */
public interface IEnterprise {
	/**
	 * get systems
	 * 
	 * @return
	 */
	Collection<ISystem> getSystems();

	/**
	 * get all started systems
	 * 
	 * @return
	 */
	Collection<ISystem> getStartedSystems();

	/**
	 * get system by given id
	 * 
	 * @param systemId
	 * @return
	 */
	ISystem getSystem(String systemId);

	/**
	 * get started system by given id
	 * 
	 * @param systemId
	 * @return
	 */
	ISystem getStartedSystem(String systemId);

	/**
	 * prepare system, not startup
	 * 
	 * @param system
	 * @return
	 */
	void prepareSystem(ISystem system);

	/**
	 * startup enterprise
	 * 
	 * @return
	 */
	void startup();

	/**
	 * startup system, system should be appended into enterprise
	 * 
	 * @param systemId
	 * @return
	 */
	void startupSystem(String systemId);

	/**
	 * startup system. if the same system existed, shutdown the old one
	 * 
	 * @param system
	 * @return
	 */
	void startupSystem(ISystem system);

	/**
	 * check the status of system by given system id
	 * 
	 * @param systemId
	 * @return
	 */
	boolean isSystemStartup(String systemId);

	/**
	 * shutdown enterprise
	 * 
	 * @return
	 */
	void shutdown();

	/**
	 * shutdown system
	 * 
	 * @param systemId
	 * @return
	 */
	void shutdownSystem(String systemId);

	/**
	 * find resource by given qualified id, each segment should be separated by
	 * {@linkplain IResource#SEPARATOR}
	 * 
	 * @param qualifiedResourceId
	 * @return
	 */
	<T extends IResource> T findResource(String qualifiedResourceId);
}

/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;

/**
 * Enterprise
 * 
 * @author brad.wu
 */
public interface IEnterprise {
	/**
	 * get applications
	 * 
	 * @return
	 */
	Collection<IApplication> getApplications();

	/**
	 * get all started applications
	 * 
	 * @return
	 */
	Collection<IApplication> getStartedApplications();

	/**
	 * get application by given id
	 * 
	 * @param applicationId
	 * @return
	 */
	IApplication getApplication(String applicationId);

	/**
	 * get started application by given id
	 * 
	 * @param applicationId
	 * @return
	 */
	IApplication getStartedApplication(String applicationId);

	/**
	 * prepare application, not startup
	 * 
	 * @param application
	 * @return
	 */
	void prepareApplication(IApplication application);

	/**
	 * startup enterprise
	 * 
	 * @return
	 */
	void startup();

	/**
	 * startup application, application should be appended into enterprise
	 * 
	 * @param applicationId
	 * @return
	 */
	void startupApplication(String applicationId);

	/**
	 * startup application. if the same application existed, shutdown the old
	 * one
	 * 
	 * @param application
	 * @return
	 */
	void startupApplication(IApplication application);

	/**
	 * check the status of application by given application id
	 * 
	 * @param applicationId
	 * @return
	 */
	boolean isApplicationStartup(String applicationId);

	/**
	 * shutdown enterprise
	 * 
	 * @return
	 */
	void shutdown();

	/**
	 * shutdown application
	 * 
	 * @param applicationId
	 * @return
	 */
	void shutdownApplication(String applicationId);

	/**
	 * find resource by given qualified id, each segment should be separated by
	 * {@linkplain IResource#SEPARATOR}
	 * 
	 * @param qualifiedResourceId
	 * @return
	 */
	<T extends IResource> T findResource(String qualifiedResourceId);
}

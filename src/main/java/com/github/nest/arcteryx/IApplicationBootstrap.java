/**
 * 
 */
package com.github.nest.arcteryx;

/**
 * application bootstrap
 * 
 * @author brad.wu
 */
public interface IApplicationBootstrap {
	/**
	 * start up application
	 */
	void startup();

	/**
	 * check the application is started or not
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * shut down application
	 */
	void shutdown();

	/**
	 * get application
	 * 
	 * @return
	 */
	IApplication getApplication();
}

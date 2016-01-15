/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * System.</br>
 * System contains applications, application contains components, component
 * contains components and resources.
 * 
 * @author brad.wu
 * @see IApplication
 * @see IComponent
 * @see IResource
 */
public interface ISystem extends IContainer {
	/**
	 * get application by given id
	 * 
	 * @param id
	 * @return
	 */
	IApplication getApplication(String id);

	/**
	 * get derivation system
	 * 
	 * @return
	 */
	ISystem getDerivation();
}

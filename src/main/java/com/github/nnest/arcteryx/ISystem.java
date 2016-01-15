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
	 * get derivation system
	 * 
	 * @return
	 */
	ISystem getDerivation();

	/**
	 * set derivation system
	 * 
	 * @param derivation
	 */
	void setDerivation(ISystem derivation);
}

/**
 * 
 */
package com.github.nnest.arcteryx.event;

/**
 * Resource event dispatcher.<br/>
 * Must be stateless.
 * 
 * @author brad.wu
 */
public interface IResourceEventDispatcher<E extends IResourceEvent, L extends IResourceEventListener> {
	/**
	 * dispatch event
	 * 
	 * @param evt
	 * @param listeners
	 */
	void dispatch(E evt, L... listeners);
}

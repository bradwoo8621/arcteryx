/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Resource layer.</br>
 * Not necessary, but to identity resource more accurately.
 * 
 * @author brad.wu
 */
public interface ILayer {
	/**
	 * get id
	 * 
	 * @return
	 */
	String getId();

	/**
	 * get parent layer id
	 * 
	 * @return
	 */
	String getParentId();
}

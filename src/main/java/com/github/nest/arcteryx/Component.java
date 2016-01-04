/**
 * 
 */
package com.github.nest.arcteryx;

/**
 * Component
 * 
 * @author brad.wu
 */
public class Component extends AbstractContainer implements IComponent {
	public Component(String id) {
		super(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IComponent#getApplication()
	 */
	@Override
	public IApplication getApplication() {
		return (IApplication) this.getContainer();
	}
}

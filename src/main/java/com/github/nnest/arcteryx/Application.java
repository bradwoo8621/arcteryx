/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Application.</br>
 * Only accepts {@linkplain IComponent} as child resource.
 * 
 * @author brad.wu
 */
public class Application extends AbstractContainer implements IApplication {
	public Application(String id) {
		super(id);
	}

	/**
	 * Child of application must be one of {@linkplain @IComponent}.
	 * 
	 * @see com.github.nnest.arcteryx.AbstractContainer#accepted(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public boolean accepted(IResource resource) {
		if (resource instanceof IComponent) {
			return true;
		} else {
			this.getLogger().error("Resource[{}] must be an instance of {}", resource.getId(), IComponent.class);
			return false;
		}
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Application
 * 
 * @author brad.wu
 */
public class Application extends AbstractContainer implements IApplication {
	public Application(String id) {
		super(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.AbstractContainer#accepted(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public boolean accepted(IResource resource) {
		if (!(resource instanceof IComponent)) {
			this.getLogger().info("Resource[{}] must be an instance of {}", resource.getId(), IComponent.class);
			return false;
		}
		return super.accepted(resource);
	}
}

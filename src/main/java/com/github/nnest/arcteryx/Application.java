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
		if (resource instanceof IApplication) {
			// application must has same id with its parent
			if (!resource.getId().equals(this.getId())) {
				this.getLogger().error("Application[{}] must have same id with its parent[{}]", resource.getId(),
						this.getId());
				return false;
			} else {
				return true;
			}
		} else if (resource instanceof IComponent) {
			return true;
		} else {
			this.getLogger().info("Resource[{}] must be an instance of {} or {}", resource.getId(), IApplication.class,
					IComponent.class);
			return false;
		}
	}
}

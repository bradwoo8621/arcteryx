/**
 * 
 */
package com.github.nnest.arcteryx;

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
	 * @see com.github.nnest.arcteryx.IComponent#getApplication()
	 */
	@Override
	public IApplication getApplication() {
		return (IApplication) this.getContainer();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.AbstractContainer#accepted(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public boolean accepted(IResource resource) {
		if (resource instanceof IApplication || resource instanceof IComponent) {
			// application must has same id with its parent
			this.getLogger().error("Resource[{}] cannot be an instance of {} or {}", resource.getId(),
					IApplication.class, IComponent.class);
			return false;
		} else {
			return true;
		}
	}
}

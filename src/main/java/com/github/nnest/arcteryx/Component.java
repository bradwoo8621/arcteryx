/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Component</br>
 * {@linkplain IApplication} is not accepted as child resource.
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
	 * Child of component cannot be {@linkplain IApplication}
	 * 
	 * @see com.github.nnest.arcteryx.AbstractContainer#accepted(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public boolean accepted(IResource resource) {
		if (resource instanceof IApplication) {
			// application must has same id with its parent
			this.getLogger().error("Resource[{}] cannot be an instance of {}", resource.getId(), IApplication.class);
			return false;
		} else {
			return true;
		}
	}
}

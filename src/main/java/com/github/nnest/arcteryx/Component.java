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
	 * Child of component cannot be {@linkplain IApplication} or
	 * {@linkplain ISystem}
	 * 
	 * @see com.github.nnest.arcteryx.AbstractContainer#accepted(com.github.nnest.arcteryx.IResource)
	 */
	@Override
	public boolean accepted(IResource resource) {
		if (resource instanceof IApplication || resource instanceof ISystem) {
			// application must has same id with its parent
			this.getLogger().error("Resource[{}] cannot be an instance of {} or {}", resource.getId(),
					IApplication.class, ISystem.class);
			return false;
		} else {
			return true;
		}
	}
}

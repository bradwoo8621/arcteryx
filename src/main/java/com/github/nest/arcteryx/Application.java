/**
 * 
 */
package com.github.nest.arcteryx;

import java.util.ArrayList;
import java.util.Collection;

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
	 * @see com.github.nest.arcteryx.IApplication#findComponent(java.lang.String)
	 */
	@Override
	public IComponent findComponent(String componentId) throws ResourceNotFoundException {
		return (IComponent) this.findResource(componentId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IApplication#registerComponent(com.github.nest.arcteryx.IComponent)
	 */
	@Override
	public IComponent registerComponent(IComponent component) {
		return (IComponent) this.registerResource(component);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IApplication#unregisterComponent(java.lang.String)
	 */
	@Override
	public IComponent unregisterComponent(String componentId) {
		return (IComponent) this.unregisterResource(componentId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.IApplication#getAllComponents()
	 */
	@Override
	public Collection<IComponent> getAllComponents() {
		Collection<IComponent> list = new ArrayList<IComponent>();
		for (IResource resource : this.getAllResources()) {
			list.add((IComponent) resource);
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nest.arcteryx.AbstractContainer#registerResource(com.github.nest.arcteryx.IResource)
	 */
	@Override
	public IResource registerResource(IResource resource) {
		if (resource != null && !(resource instanceof IComponent)) {
			throw new IllegalArgumentException(
					"Resource[" + resource.getId() + "] must be an instance of " + IComponent.class);
		}
		return super.registerResource(resource);
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Application.</br>
 * Only accepts {@linkplain IApplication} and {@linkplain IComponent} as child
 * resource. For {@linkplain IApplication} as child, accepts which has same id with
 * current application.
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
	 * @see com.github.nnest.arcteryx.AbstractContainer#findResource(java.lang.String)
	 */
	@Override
	public <T extends IResource> T findResource(String resourceId) {
		T resource = this.findResourceInChildApplications(resourceId);
		if (resource == null) {
			return super.findResource(resourceId);
		} else {
			return resource;
		}
	}

	/**
	 * find resource recursive
	 * 
	 * @param resourceId
	 * @return
	 */
	protected <T extends IResource> T findResourceInChildApplications(String resourceId) {
		Collection<IApplication> childApplications = this.getResources(IApplication.class);
		if (childApplications != null) {
			for (IApplication childApplication : childApplications) {
				T resource = childApplication.findResource(resourceId);
				if (resource != null) {
					return resource;
				}
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.AbstractContainer#findResource(java.lang.String[])
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public <T extends IResource> T findResource(String[] resourceIds) {
		T resource = null;

		// find in child application first
		Collection<IApplication> childApplications = this.getResources(IApplication.class);
		if (childApplications != null) {
			for (IApplication childApplication : childApplications) {
				resource = childApplication.findResource(resourceIds);
				if (resource != null) {
					// found, return directly
					return resource;
				}
			}
		}

		if (resource == null) {
			// not found in child applications,
			// find in myself
			IComponent component = null;
			String componentId = resourceIds[0];
			Collection<IComponent> components = this.getResources(IComponent.class);
			for (IComponent childComponent : components) {
				if (componentId.equals(childComponent.getId())) {
					component = childComponent;
					break;
				}
			}
			if (component == null) {
				if (this.getLogger().isErrorEnabled()) {
					this.getLogger().error("Resource[{}] not found cause by [{}] in container [{}] not found", //
							StringUtils.join(resourceIds, IResource.SEPARATOR), //
							componentId, this.getQualifiedId());
				}
				return null;
			} else if (resourceIds.length == 1) {
				return (T) component;
			} else {
				return component.findResource(ArrayUtils.subarray(resourceIds, 1, resourceIds.length));
			}
		}

		// dead code, never occurs
		return resource;
	}

	/**
	 * Child of application must be one of {@linkplain IApplication} or
	 * {@linkplain @IComponent}, if it is an {@linkplain IApplication}, id of
	 * application must be same as container.
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
			this.getLogger().error("Resource[{}] must be an instance of {} or {}", resource.getId(), IApplication.class,
					IComponent.class);
			return false;
		}
	}
}

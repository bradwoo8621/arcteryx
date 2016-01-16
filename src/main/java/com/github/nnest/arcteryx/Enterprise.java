/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * enterprise.
 * 
 * @author brad.wu
 */
public class Enterprise implements IEnterprise {
	private Systems systems = new Systems();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getSystems()
	 */
	@Override
	public Collection<ISystem> getSystems() {
		return this.systems.all();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getStartedSystems()
	 */
	@Override
	public Collection<ISystem> getStartedSystems() {
		return this.systems.allStarted();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getSystem(java.lang.String)
	 */
	@Override
	public ISystem getSystem(String systemId) {
		return this.systems.get(systemId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getStartedSystem(java.lang.String)
	 */
	@Override
	public ISystem getStartedSystem(String systemId) {
		ISystem system = this.systems.getStarted(systemId);
		if (system == null) {
			throw new SystemNotStartException("System [" + systemId + "] not found in started systems");
		} else {
			return system;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#prepareSystem(com.github.nnest.arcteryx.ISystem)
	 */
	@Override
	public void prepareSystem(ISystem system) {
		if (system == null) {
			throw new IllegalArgumentException("System cannot be null when do preparation");
		}

		synchronized (this.systems) {
			String systemId = system.getId();
			ISystem origin = this.systems.get(systemId);
			if (origin != system) {
				if (origin != null && this.isSystemStartup(systemId)) {
					// if original system was started, shut down first
					this.shutdownSystem(systemId);
				}
				// prepare new system
				this.systems.put(system);
			} else {
				this.getLogger().info("System[{}] already prepared", systemId);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#startup()
	 */
	@Override
	public void startup() {
		this.getLogger().info("Enterprise starting up");
		synchronized (this.systems) {
			this.systems.startAll();
		}
		this.getLogger().info("Enterprise started up");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#startupSystem(java.lang.String)
	 */
	@Override
	public void startupSystem(String systemId) {
		if (StringUtils.isEmpty(systemId)) {
			throw new IllegalArgumentException("System ID[" + systemId + "] cannot be null when startup");
		}
		this.getLogger().info("System[{}] starting up", systemId);
		synchronized (this.systems) {
			ISystem system = this.systems.get(systemId);
			if (system != null) {
				this.systems.start(system);
			} else {
				throw new ResourceNotFoundException("System[" + systemId + "] not found when startup");
			}

		}
		this.getLogger().info("System[{}] started up", systemId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#startupSystem(com.github.nnest.arcteryx.ISystem)
	 */
	@Override
	public void startupSystem(ISystem system) {
		if (system == null) {
			throw new IllegalArgumentException("System cannot be null when startup");
		}
		this.getLogger().info("System[{}] starting up", system.getId());
		synchronized (this.systems) {
			this.systems.start(system);
		}
		this.getLogger().info("System[{}] started up", system.getId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#isSystemStartup(java.lang.String)
	 */
	@Override
	public boolean isSystemStartup(String systemId) {
		return this.systems.isStarted(systemId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#shutdown()
	 */
	@Override
	public void shutdown() {
		synchronized (this.systems) {
			this.systems.shutdownAll();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#shutdownSystem(java.lang.String)
	 */
	@Override
	public void shutdownSystem(String systemId) {
		synchronized (this.systems) {
			this.systems.shutdown(systemId);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#findResource(java.lang.String)
	 */
	@Override
	public <T extends IResource> T findResource(String absolutePath) {
		String redressedAbsolutePath = ResourceUtils.redressResourcePath(absolutePath);
		if (StringUtils.isEmpty(redressedAbsolutePath)) {
			throw new IllegalArgumentException("Resource path cannot be null");
		}

		this.getLogger().debug("Get resource by path [{}]", redressedAbsolutePath);
		String[] pathSegments = redressedAbsolutePath.split(IResource.SEPARATOR);
		if (this.getLogger().isDebugEnabled()) {
			// check enabled to prevent the join cost
			// change separator to compare segments and give path
			this.getLogger().debug("Resource path segments [{}]", new Object[] { pathSegments });
		}

		// find started system by first segment
		T resource = this.findResource(pathSegments);
		if (resource == null) {
			this.getLogger().warn("Resource [{}] not found", absolutePath);
		}
		return resource;
	}

	/**
	 * find resource
	 * 
	 * @param resourcePathSegments
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends IResource> T findResource(String[] resourcePathSegments) {
		ISystem system = this.getStartedSystem(resourcePathSegments[0]);
		if (resourcePathSegments.length == 1) {
			return (T) system;
		} else {
			String[] idRelatedToSystem = ArrayUtils.subarray(resourcePathSegments, 1, resourcePathSegments.length);
			T resource = system.findResource(idRelatedToSystem);
			if (resource == null) {
				ISystem derivation = system.getDerivation();
				while (derivation != null) {
					resource = derivation.findResource(idRelatedToSystem);
					if (resource != null) {
						return resource;
					} else {
						derivation = derivation.getDerivation();
					}
				}
				// not found in derivations
				return null;
			} else {
				return resource;
			}
		}
	}

	/**
	 * get logger
	 * 
	 * @return
	 */
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/**
	 * Top level systems container, for synchronization operations purpose
	 * 
	 * @author brad.wu
	 */
	private static class Systems {
		private Map<String, ISystem> systems = new HashMap<String, ISystem>();
		private Map<String, ISystem> startedSystems = new HashMap<String, ISystem>();

		/**
		 * get all systems
		 * 
		 * @return
		 */
		public Collection<ISystem> all() {
			return this.systems.values();
		}

		/**
		 * get all started systems
		 * 
		 * @return
		 */
		public Collection<ISystem> allStarted() {
			return this.startedSystems.values();
		}

		/**
		 * get system by given id
		 * 
		 * @param systemId
		 * @return
		 */
		public ISystem get(String systemId) {
			return this.systems.get(systemId);
		}

		/**
		 * get started system by given id
		 * 
		 * @param systemId
		 * @return
		 */
		public ISystem getStarted(String systemId) {
			return this.startedSystems.get(systemId);
		}

		/**
		 * put system
		 * 
		 * @param system
		 */
		public void put(ISystem system) {
			this.systems.put(system.getId(), system);
		}

		/**
		 * start all systems
		 */
		public void startAll() {
			this.shutdownAll();
			this.startedSystems.putAll(this.systems);
		}

		/**
		 * start appointed system
		 * 
		 * @param system
		 */
		public void start(ISystem system) {
			this.put(system);
			this.startedSystems.put(system.getId(), system);
		}

		/**
		 * check status by given system id
		 * 
		 * @param systemId
		 * @return
		 */
		public boolean isStarted(String systemId) {
			return this.startedSystems.containsKey(systemId);
		}

		/**
		 * shutdown all systems
		 */
		public void shutdownAll() {
			this.startedSystems.clear();
		}

		/**
		 * shutdown by given system id
		 * 
		 * @param systemId
		 */
		public void shutdown(String systemId) {
			this.startedSystems.remove(systemId);
		}
	}
}

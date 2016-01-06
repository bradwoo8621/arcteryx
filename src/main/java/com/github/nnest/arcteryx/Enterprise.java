/**
 * 
 */
package com.github.nnest.arcteryx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * enterprise
 * 
 * @author brad.wu
 */
public class Enterprise implements IEnterprise {
	private Applications applications = new Applications();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getApplications()
	 */
	@Override
	public Collection<IApplication> getApplications() {
		return this.applications.all();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getStartedApplications()
	 */
	@Override
	public Collection<IApplication> getStartedApplications() {
		return this.applications.allStarted();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#getApplication(java.lang.String)
	 */
	@Override
	public IApplication getApplication(String applicationId) {
		IApplication application = this.applications.get(applicationId);
		if (application == null) {
			throw new ResourceNotFoundException("Application[" + applicationId + "] not found");
		} else {
			return application;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#prepareApplication(com.github.nnest.arcteryx.IApplication)
	 */
	@Override
	public void prepareApplication(IApplication application) {
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null when do preparation");
		}

		synchronized (this.applications) {
			String applicationId = application.getId();
			IApplication origin = this.applications.get(applicationId);
			if (origin != application) {
				if (origin != null && this.isApplicationStartup(applicationId)) {
					// if original application was started, shut down first
					this.shutdownApplication(applicationId);
				}
				// prepare new application
				this.applications.put(application);
			} else {
				this.getLogger().info("Application[{}] already prepared", applicationId);
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
		synchronized (this.applications) {
			this.applications.startAll();
		}
		this.getLogger().info("Enterprise started up");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#startupApplication(java.lang.String)
	 */
	@Override
	public void startupApplication(String applicationId) {
		if (Strings.isNullOrEmpty(applicationId)) {
			throw new IllegalArgumentException("Application ID[" + applicationId + "] cannot be null when startup");
		}
		this.getLogger().info("Application[{}] starting up", applicationId);
		synchronized (this.applications) {
			IApplication application = this.applications.get(applicationId);
			if (application != null) {
				this.applications.start(application);
			} else {
				throw new ResourceNotFoundException("Application[" + applicationId + "] not found when startup");
			}

		}
		this.getLogger().info("Application[{}] started up", applicationId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#startupApplication(com.github.nnest.arcteryx.IApplication)
	 */
	@Override
	public void startupApplication(IApplication application) {
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null when startup");
		}
		this.getLogger().info("Application[{}] starting up", application.getId());
		synchronized (this.applications) {
			this.applications.start(application);
		}
		this.getLogger().info("Application[{}] started up", application.getId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#isApplicationStartup(java.lang.String)
	 */
	@Override
	public boolean isApplicationStartup(String applicationId) {
		return this.applications.isStarted(applicationId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#shutdown()
	 */
	@Override
	public void shutdown() {
		synchronized (this.applications) {
			this.applications.shutdownAll();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.IEnterprise#shutdownApplication(java.lang.String)
	 */
	@Override
	public void shutdownApplication(String applicationId) {
		synchronized (this.applications) {
			this.applications.shutdown(applicationId);
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

	private static class Applications {
		private Map<String, IApplication> applications = new HashMap<String, IApplication>();
		private Map<String, IApplication> startedApplications = new HashMap<String, IApplication>();

		/**
		 * get all applications
		 * 
		 * @return
		 */
		public Collection<IApplication> all() {
			return this.applications.values();
		}

		/**
		 * get all started applications
		 * 
		 * @return
		 */
		public Collection<IApplication> allStarted() {
			return this.startedApplications.values();
		}

		/**
		 * get application by given id
		 * 
		 * @param applicationId
		 * @return
		 */
		public IApplication get(String applicationId) {
			return this.applications.get(applicationId);
		}

		/**
		 * put application
		 * 
		 * @param application
		 */
		public void put(IApplication application) {
			this.applications.put(application.getId(), application);
		}

		/**
		 * start all applications
		 */
		public void startAll() {
			this.shutdownAll();
			this.startedApplications.putAll(this.applications);
		}

		/**
		 * start appointed application
		 * 
		 * @param application
		 */
		public void start(IApplication application) {
			this.put(application);
			this.startedApplications.put(application.getId(), application);
		}

		/**
		 * check status by given application id
		 * 
		 * @param applicationId
		 * @return
		 */
		public boolean isStarted(String applicationId) {
			return this.startedApplications.containsKey(applicationId);
		}

		/**
		 * shutdown all applications
		 */
		public void shutdownAll() {
			this.startedApplications.clear();
		}

		/**
		 * shutdown by given application id
		 * 
		 * @param applicationId
		 */
		public void shutdown(String applicationId) {
			this.startedApplications.remove(applicationId);
		}
	}
}

/**
 * 
 */
package com.github.nest.arcteryx.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.nest.arcteryx.AbstractResource;

/**
 * @author brad.wu
 *
 */
public class ResourceLifecycleEventTest {
	@Test
	public void testConstruct() {
		new Resource("test") {

			/**
			 * (non-Javadoc)
			 * 
			 * @see com.github.nest.arcteryx.AbstractResource#createEventListeners()
			 */
			@Override
			protected IResourceEventListeners createEventListeners() {
				IResourceEventListeners listeners = super.createEventListeners();
				listeners.addListener(new LifecycleListener(), IResourceLifecycleEventListener.class);
				return listeners;
			}
		};
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}

	private static class LifecycleListener extends ResourceLifecycleEventAdapter {

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.event.ResourceLifecycleEventAdapter#resourceDidConstruct(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceDidConstruct(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
		}

	}
}

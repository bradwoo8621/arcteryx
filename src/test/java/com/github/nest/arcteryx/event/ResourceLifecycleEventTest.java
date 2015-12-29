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

	@Test
	public void testDestory() {
		Resource r = new Resource("test");
		LifecycleListener listener = new LifecycleListener();
		r.addLifecycleListener(listener);
		r.destroy();
		assertEquals(3, listener.getCount());
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}

	private static class LifecycleListener extends ResourceLifecycleEventAdapter {
		private int count = 0;
		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.event.ResourceLifecycleEventAdapter#resourceDidConstruct(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceDidConstruct(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_DID_CONSTRUCT, evt.getEventType());
			count++;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.event.ResourceLifecycleEventAdapter#resourceShouldDestroy(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceShouldDestroy(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_SHOULD_DESTROY, evt.getEventType());
			count++;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.event.ResourceLifecycleEventAdapter#resourceWillDestroy(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceWillDestroy(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_WILL_DESTROY, evt.getEventType());
			count++;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.event.ResourceLifecycleEventAdapter#resourceDidDestory(com.github.nest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceDidDestory(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_DID_DESTROY, evt.getEventType());
			count++;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.nnest.arcteryx.AbstractResource;
import com.github.nnest.arcteryx.event.IResourceEventListeners;
import com.github.nnest.arcteryx.event.IResourceLifecycleEventListener;
import com.github.nnest.arcteryx.event.ResourceLifecycleEvent;
import com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter;

/**
 * @author brad.wu
 *
 */
public class ResourceLifecycleEventTest {
	@Test
	public void testConstruct() {
		final LifecycleListener listener = new LifecycleListener();
		new Resource("test") {

			/**
			 * (non-Javadoc)
			 * 
			 * @see com.github.nnest.arcteryx.AbstractResource#createEventListeners()
			 */
			@Override
			protected IResourceEventListeners createEventListeners() {
				IResourceEventListeners listeners = super.createEventListeners();
				listeners.addListener(listener, IResourceLifecycleEventListener.class);
				return listeners;
			}
		};
		assertEquals("DID_CONSTRUCT event should be handled.", 1, listener.getCount());
	}

	@Test
	public void testDestory() {
		Resource r = new Resource("test");
		LifecycleListener listener = new LifecycleListener();
		r.addLifecycleListener(listener);
		r.destroy();
		assertEquals("Three destory events should be handled.", 14, listener.getCount());
	}

	@Test
	public void testStopWithShouldNot() {
		Resource r = new Resource("test");
		LifecycleListener listener = new LifecycleListener() {

			/**
			 * (non-Javadoc)
			 * 
			 * @see com.github.nnest.arcteryx.event.ResourceLifecycleEventTest.LifecycleListener#resourceShouldDestroy(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
			 */
			@Override
			public void resourceShouldDestroy(ResourceLifecycleEvent evt) {
				super.resourceShouldDestroy(evt);
				evt.setShouldNot(true);
			}

		};
		r.addLifecycleListener(listener);
		r.destroy();
		assertEquals(2, listener.getCount());
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
		 * @see com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter#resourceDidConstruct(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
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
		 * @see com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter#resourceShouldDestroy(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceShouldDestroy(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_SHOULD_DESTROY, evt.getEventType());
			count += 2;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter#resourceWillDestroy(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceWillDestroy(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_WILL_DESTROY, evt.getEventType());
			count += 4;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter#resourceDidDestory(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
		 */
		@Override
		public void resourceDidDestory(ResourceLifecycleEvent evt) {
			assertEquals("test", evt.getEventTarget().getId());
			assertEquals(ResourceLifecycleEvent.EventType.RESOURCE_DID_DESTROY, evt.getEventType());
			count += 8;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}
	}
}

/**
 * 
 */
package com.github.nnest.arcteryx.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.nnest.arcteryx.AbstractResource;
import com.github.nnest.arcteryx.Component;
import com.github.nnest.arcteryx.event.ResourceRegistrationEvent;
import com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter;

/**
 * @author brad.wu
 *
 */
public class ResourceRegistrationEventTest {
	@Test
	public void testRegisterAndUnregister() {
		Component c = new Component("container");
		RegistrationListener listener = new RegistrationListener();
		c.addResourceRegistrationListener(listener);
		Resource r = new Resource("resource");
		c.registerResource(r);
		assertEquals("Two registration events should be handled.", 3, listener.getCount());

		c.unregisterResource(r.getId());
		assertEquals("All registration events should be handled.", 31, listener.getCount());
		
		c.removeResourceRegistrationListener(listener);
		c.registerResource(r);
		assertEquals(31, listener.getCount());
	}

	@Test
	public void testStopWithShouldNot() {
		Component c = new Component("container");
		RegistrationListener listener = new RegistrationListener() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventTest.RegistrationListener#resourceShouldUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
			 */
			@Override
			public void resourceShouldUnregister(ResourceRegistrationEvent evt) {
				super.resourceShouldUnregister(evt);
				evt.setShouldNot(true);
			}
		};
		c.addResourceRegistrationListener(listener);
		Resource r = new Resource("resource");
		c.registerResource(r);

		c.unregisterResource(r.getId());
		assertEquals("Three registration events before WILL_UNREGISTRATION (not contains) should be handled.", 7,
				listener.getCount());
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}

	private static class RegistrationListener extends ResourceRegistrationEventAdapter {
		private int count = 0;

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter#resourceWillRegister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
		 */
		@Override
		public void resourceWillRegister(ResourceRegistrationEvent evt) {
			assertEquals("container", evt.getContainer().getId());
			assertEquals("resource", evt.getResource().getId());
			assertEquals(ResourceRegistrationEvent.EventType.RESOURCE_WILL_REGISTER, evt.getEventType());
			count++;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter#resourceDidRegister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
		 */
		@Override
		public void resourceDidRegister(ResourceRegistrationEvent evt) {
			assertEquals("container", evt.getContainer().getId());
			assertEquals("resource", evt.getResource().getId());
			assertEquals(ResourceRegistrationEvent.EventType.RESOURCE_DID_REGISTER, evt.getEventType());
			count += 2;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter#resourceShouldUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
		 */
		@Override
		public void resourceShouldUnregister(ResourceRegistrationEvent evt) {
			assertEquals("container", evt.getContainer().getId());
			assertEquals("resource", evt.getResource().getId());
			assertEquals(ResourceRegistrationEvent.EventType.RESOURCE_SHOULD_UNREGISTER, evt.getEventType());
			count += 4;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter#resourceWillUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
		 */
		@Override
		public void resourceWillUnregister(ResourceRegistrationEvent evt) {
			assertEquals("container", evt.getContainer().getId());
			assertEquals("resource", evt.getResource().getId());
			assertEquals(ResourceRegistrationEvent.EventType.RESOURCE_WILL_UNREGISTER, evt.getEventType());
			count += 8;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter#resourceDidUnregister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
		 */
		@Override
		public void resourceDidUnregister(ResourceRegistrationEvent evt) {
			assertEquals("container", evt.getEventTarget().getId());
			assertEquals("resource", evt.getResource().getId());
			assertEquals(ResourceRegistrationEvent.EventType.RESOURCE_DID_UNREGISTER, evt.getEventType());
			count += 16;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}
	}
}

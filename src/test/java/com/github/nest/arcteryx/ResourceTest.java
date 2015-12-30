/**
 * 
 */
package com.github.nest.arcteryx;

import org.junit.Test;

import com.github.nest.arcteryx.event.IResourceEvent;
import com.github.nest.arcteryx.event.ResourceLifecycleEvent;
import com.github.nest.arcteryx.event.ResourceRegistrationEvent;

/**
 * @author brad.wu
 *
 */
public class ResourceTest {
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWithNull() {
		new Resource(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructWithEmpty() {
		new Resource("");
	}

	@Test
	public void testFireEvent() {
		Resource r = new Resource("test");
		r.fireEvent(new ResourceLifecycleEvent(r, ResourceLifecycleEvent.EventType.RESOURCE_DID_DESTROY));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFireUnsupportedEvent() {
		Resource r = new Resource("test");
		r.fireEvent(new ResourceRegistrationEvent(new Component("container"), r,
				ResourceRegistrationEvent.EventType.RESOURCE_DID_REGISTER));
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.AbstractResource#fireEvent(com.github.nest.arcteryx.event.IResourceEvent)
		 */
		@Override
		public void fireEvent(IResourceEvent evt) {
			super.fireEvent(evt);
		}
	}
}

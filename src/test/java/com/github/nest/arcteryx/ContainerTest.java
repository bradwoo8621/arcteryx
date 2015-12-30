/**
 * 
 */
package com.github.nest.arcteryx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.nest.arcteryx.event.IResourceEvent;
import com.github.nest.arcteryx.event.ResourceRegistrationEvent;

/**
 * @author brad.wu
 *
 */
public class ContainerTest {
	@Test
	public void testFireEvent() {
		App a = new App("app");
		a.fireEvent(new ResourceRegistrationEvent(a, new Resource("test"),
				ResourceRegistrationEvent.EventType.RESOURCE_DID_REGISTER));
	}

	@Test
	public void testRegister() {
		App a = new App("app");
		Component c1 = new Component("c1");
		Component c2 = new Component("c2");

		a.registerResource(c1);
		assertEquals(1, a.getAllResources().size());
		assertEquals(1, a.getAllComponents().size());

		a.registerResource(c2);
		assertEquals(2, a.getAllResources().size());
		assertEquals(2, a.getAllComponents().size());

		a.registerResource(c2);
		assertEquals(2, a.getAllResources().size());
		assertEquals(2, a.getAllComponents().size());

		assertEquals(c1, a.findResource(c1.getId()));
		assertEquals(c2, a.findResource(c2.getId()));

		a.unregisterResource(c1.getId());
		assertEquals(1, a.getAllResources().size());
		assertEquals(1, a.getAllComponents().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterNull() {
		App a = new App("app");
		a.registerResource(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnregisterNull() {
		App a = new App("app");
		a.unregisterComponent(null);
	}

	private static class App extends Application {
		public App(String id) {
			super(id);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nest.arcteryx.AbstractContainer#fireEvent(com.github.nest.arcteryx.event.IResourceEvent)
		 */
		@Override
		public void fireEvent(IResourceEvent evt) {
			super.fireEvent(evt);
		}
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}
}

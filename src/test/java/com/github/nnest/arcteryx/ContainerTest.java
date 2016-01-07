/**
 * 
 */
package com.github.nnest.arcteryx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.github.nnest.arcteryx.event.IResourceEvent;
import com.github.nnest.arcteryx.event.ResourceLifecycleEvent;
import com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter;
import com.github.nnest.arcteryx.event.ResourceRegistrationEvent;
import com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter;

/**
 * @author brad.wu
 *
 */
public class ContainerTest {
	@Test
	public void testFireEvent() {
		final App a = new App("app");
		final Component c = new Component("comp");
		a.addResourceRegistrationListener(new ResourceRegistrationEventAdapter() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.github.nnest.arcteryx.event.ResourceRegistrationEventAdapter#resourceDidRegister(com.github.nnest.arcteryx.event.ResourceRegistrationEvent)
			 */
			@Override
			public void resourceDidRegister(ResourceRegistrationEvent evt) {
				assertEquals(evt.getEventTarget(), a);
				assertEquals(evt.getContainer(), a);
				assertEquals(evt.getResource(), c);
			}
		});
		a.addLifecycleListener(new ResourceLifecycleEventAdapter() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.github.nnest.arcteryx.event.ResourceLifecycleEventAdapter#resourceDidConstruct(com.github.nnest.arcteryx.event.ResourceLifecycleEvent)
			 */
			@Override
			public void resourceDidConstruct(ResourceLifecycleEvent evt) {
				assertEquals(evt.getEventTarget(), a);
			}
		});
		a.fireEvent(new ResourceRegistrationEvent(a, c, ResourceRegistrationEvent.EventType.RESOURCE_DID_REGISTER));
		a.fireEvent(new ResourceLifecycleEvent(a, ResourceLifecycleEvent.EventType.RESOURCE_DID_CONSTRUCT));
	}

	@Test
	public void testRegister() {
		App a = new App("app");
		Component c1 = new Component("c1");
		Component c2 = new Component("c2");
		Component c3 = new CustomComponent("c3");

		a.registerResource(c1);
		assertEquals(1, a.getResources().size());

		a.registerResource(c2);
		assertEquals(2, a.getResources().size());

		a.registerResource(c2);
		assertEquals(2, a.getResources().size());

		assertEquals(c1, a.findResource(c1.getId()));
		assertEquals(c2, a.findResource(c2.getId()));

		a.unregisterResource(c1.getId());
		assertEquals(1, a.getResources().size());

		a.registerResource(c3);
		assertEquals(2, a.getResources(IComponent.class).size());
		assertEquals(2, a.getResources(Component.class).size());
		assertEquals(1, a.getResources(CustomComponent.class).size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterNull() {
		App a = new App("app");
		a.registerResource(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnregisterNull() {
		App a = new App("app");
		a.unregisterResource(null);
	}

	public void testGetUnexistedResource() {
		App a = new App("app");
		assertNull(a.findResource("test"));
	}

	@Test
	public void testChildType() {
		App a = new App("app");
		Component comp = new Component("comp");
		a.registerResource(comp);
		assertEquals(comp, a.findResource("comp"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongChildType() {
		App a = new App("app");
		Resource res = new Resource("res");
		a.registerResource(res);
	}

	@Test
	public void testFindResource() {
		Application app1 = new Application("a1");
		Component comp11 = new Component("c11");
		comp11.setContainer(app1);
		app1.registerResource(comp11);
		Resource res111 = new Resource("r111");
		res111.setContainer(comp11);
		comp11.registerResource(res111);
		// a1/c11/r111

		assertEquals(comp11, app1.findResource("c11"));
		assertEquals(res111, app1.findResource(StringUtils.join(new String[] { "c11", "r111" }, IResource.SEPARATOR)));

		assertNull(app1.findResource("n1"));
		assertNull(app1.findResource("n1/n2"));

		assertNull(app1.findResource("c11/r111/n1"));

		Application app2 = new Application("a1");
		app2.setContainer(app1);
		app1.registerResource(app2);
		// a1/c11/r111
		// a1/a1

		assertEquals(comp11, app1.findResource("c11"));
		assertEquals(res111, app1.findResource(StringUtils.join(new String[] { "c11", "r111" }, IResource.SEPARATOR)));

		Component comp21 = new Component("c11");
		comp21.setContainer(app2);
		app2.registerResource(comp21);
		// a1/c11/r111
		// a1/a1/c11

		assertEquals(comp21, app1.findResource("c11"));
		assertEquals(res111, app1.findResource(StringUtils.join(new String[] { "c11", "r111" }, IResource.SEPARATOR)));

		Component comp12 = new Component("c12");
		comp12.setContainer(app1);
		app1.registerResource(comp12);
		// a1/c11/r111
		// a1/c12
		// a1/a1/c11

		assertEquals(comp12, app1.findResource("c12"));

		Component comp23 = new Component("c13");
		comp23.setContainer(app2);
		app2.registerResource(comp23);
		// a1/c11/r111
		// a1/c12
		// a1/a1/c11
		// a1/a1/c13

		assertEquals(comp23, app1.findResource("c13"));

		comp11.unregisterResource("r111");
		res111.setContainer(comp21);
		comp21.registerResource(res111);
		// a1/c11
		// a1/c12
		// a1/a1/c11/r111
		// a1/a1/c13

		assertEquals(res111, app1.findResource("c11/r111"));

		Application app3 = new Application("a1");
		app3.setContainer(app2);
		app2.registerResource(app3);
		// a1/c11
		// a1/c12
		// a1/a1/c11/r111
		// a1/a1/c13
		// a1/a1/a1
		assertEquals(comp21, app1.findResource("c11"));
		assertEquals(comp12, app1.findResource("c12"));
		assertEquals(comp23, app1.findResource("c13"));
		assertEquals(res111, app1.findResource("c11/r111"));
		
		app2.unregisterResource("c11");
		comp21.setContainer(app3);
		app3.registerResource(comp21);
		// a1/c11
		// a1/c12
		// a1/a1/c13
		// a1/a1/a1/c11/r111
		assertEquals(comp21, app1.findResource("c11"));
		assertEquals(comp12, app1.findResource("c12"));
		assertEquals(comp23, app1.findResource("c13"));
		assertEquals(res111, app1.findResource("c11/r111"));
	}

	private static class App extends Application {
		public App(String id) {
			super(id);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see com.github.nnest.arcteryx.AbstractContainer#fireEvent(com.github.nnest.arcteryx.event.IResourceEvent)
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

	private static class CustomComponent extends Component {
		public CustomComponent(String id) {
			super(id);
		}
	}
}

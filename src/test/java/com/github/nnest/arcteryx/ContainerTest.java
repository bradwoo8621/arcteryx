/**
 * 
 */
package com.github.nnest.arcteryx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		Application app_level_1 = new Application("app_level_1");
		Component comp_level_1_1 = new Component("comp_level_1_1");
		comp_level_1_1.setContainer(app_level_1);
		app_level_1.registerResource(comp_level_1_1);
		Resource res_level_1_1_1 = new Resource("res_level_1_1_1");
		res_level_1_1_1.setContainer(comp_level_1_1);
		comp_level_1_1.registerResource(res_level_1_1_1);

		assertEquals(comp_level_1_1, app_level_1.findResource("comp_level_1_1"));
		assertEquals(res_level_1_1_1, app_level_1.findResource("comp_level_1_1/res_level_1_1_1"));

		// not existed component
		assertNull(app_level_1.findResource("comp_level_1_notexisted"));
		// not existed component or resource cause by first segment component not exists
		assertNull(app_level_1.findResource("comp_level_1_notexisted/res_level_1_1_notexisted"));
		// not existed component or resource cause by its parent is not a container
		assertNull(app_level_1.findResource("comp_level_1_1/res_level_1_1_1/res_level_1_1_notexisted"));

		Application app_level_2 = new Application("app_level_1");
		app_level_2.setContainer(app_level_1);
		app_level_1.registerResource(app_level_2);

		// no component or resource replaced, app_level_2 is empty
		assertEquals(comp_level_1_1, app_level_1.findResource("comp_level_1_1"));
		assertEquals(res_level_1_1_1, app_level_1.findResource("comp_level_1_1/res_level_1_1_1"));

		// component in app_level_2 replace in app_level_1
		Component comp_level_2_1 = new Component("comp_level_1_1");
		comp_level_2_1.setContainer(app_level_2);
		app_level_2.registerResource(comp_level_2_1);

		// find the component which replace the same id in app_Level_1
		assertEquals(comp_level_2_1, app_level_1.findResource("comp_level_1_1"));
		// resource not replaced, still find the resourc in app_level_1
		assertEquals(res_level_1_1_1, app_level_1.findResource("comp_level_1_1/res_level_1_1_1"));

		// a new component in app_level_1
		Component comp_level_1_2 = new Component("comp_level_1_2");
		comp_level_1_2.setContainer(app_level_1);
		app_level_1.registerResource(comp_level_1_2);

		// find it
		assertEquals(comp_level_1_2, app_level_1.findResource("comp_level_1_2"));

		// a new component in app_level_2
		Component comp_level_2_3 = new Component("comp_level_2_3");
		comp_level_2_3.setContainer(app_level_2);
		app_level_2.registerResource(comp_level_2_3);

		assertEquals(comp_level_2_3, app_level_1.findResource("comp_level_2_3"));

		// move resource from app_level_1 to app_level_2
		comp_level_1_1.unregisterResource("res_level_1_1_1");
		res_level_1_1_1.setContainer(comp_level_2_1);
		comp_level_2_1.registerResource(res_level_1_1_1);

		// find it
		assertEquals(res_level_1_1_1, app_level_1.findResource("comp_level_1_1/res_level_1_1_1"));

		Application app_level_3 = new Application("app_level_1");
		app_level_3.setContainer(app_level_2);
		app_level_2.registerResource(app_level_3);

		assertEquals(comp_level_2_1, app_level_1.findResource("comp_level_1_1"));
		assertEquals(comp_level_1_2, app_level_1.findResource("comp_level_1_2"));
		assertEquals(comp_level_2_3, app_level_1.findResource("comp_level_2_3"));
		assertEquals(res_level_1_1_1, app_level_1.findResource("comp_level_1_1/res_level_1_1_1"));
		
		// remove component from app_level_2 to app_level_3
		app_level_2.unregisterResource("comp_level_1_1");
		comp_level_2_1.setContainer(app_level_3);
		app_level_3.registerResource(comp_level_2_1);
		
		assertEquals(comp_level_2_1, app_level_1.findResource("comp_level_1_1"));
		assertEquals(comp_level_1_2, app_level_1.findResource("comp_level_1_2"));
		assertEquals(comp_level_2_3, app_level_1.findResource("comp_level_2_3"));
		assertEquals(res_level_1_1_1, app_level_1.findResource("comp_level_1_1/res_level_1_1_1"));
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

/**
 * 
 */
package com.github.nest.arcteryx.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.nest.arcteryx.AbstractResource;

/**
 * @author brad.wu
 *
 */
public class ListenerTest {
	@Test
	public void testEventListeners() {
		ResourceEventListeners listeners = new ResourceEventListeners();
		ResourceLifecycleEventAdapter adapter1 = new ResourceLifecycleEventAdapter();
		ResourceLifecycleEventAdapter adapter2 = new ResourceLifecycleEventAdapter();
		ResourceLifecycleEventAdapter adapter3 = new ResourceLifecycleEventAdapter();
		ResourceLifecycleEventAdapter adapter4 = new ResourceLifecycleEventAdapter();

		listeners.addListener(adapter1, IResourceLifecycleEventListener.class);
		assertEquals(1, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		
		listeners.addListener(adapter2, IResourceLifecycleEventListener.class);
		assertEquals(2, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		
		listeners.removeListener(null, IResourceLifecycleEventListener.class);
		assertEquals(2, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		
		listeners.removeListener(adapter1, IResourceLifecycleEventListener.class);
		assertEquals(1, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		assertEquals(adapter2, listeners.getListeners(IResourceLifecycleEventListener.class)[0]);
		
		listeners.addListener(adapter1, IResourceLifecycleEventListener.class);
		listeners.removeListener(adapter1, IResourceLifecycleEventListener.class);
		assertEquals(1, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		assertEquals(adapter2, listeners.getListeners(IResourceLifecycleEventListener.class)[0]);
		
		listeners.addListener(adapter1, IResourceLifecycleEventListener.class);
		listeners.addListener(adapter3, IResourceLifecycleEventListener.class);
		listeners.removeListener(adapter1, IResourceLifecycleEventListener.class);
		assertEquals(2, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		
		listeners.addListener(adapter3, IResourceLifecycleEventListener.class);
		assertEquals(2, listeners.getListeners(IResourceLifecycleEventListener.class).length);
		
		boolean removed = listeners.removeListener(adapter4, IResourceLifecycleEventListener.class);
		assertFalse("No listener removed.", removed);
	}

	@Test
	public void testAddListener() {
		Resource r = new Resource("test");
		r.addLifecycleListener(null);
		boolean removed = r.removeLifecycleListener(null);
		assertFalse("No listener removed.", removed);

		ResourceLifecycleEventAdapter adapter = new ResourceLifecycleEventAdapter();
		r.addLifecycleListener(adapter);
		removed = r.removeLifecycleListener(adapter);
		assertTrue("Listener removed", removed);
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}
}

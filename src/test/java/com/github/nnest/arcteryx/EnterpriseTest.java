/**
 * 
 */
package com.github.nnest.arcteryx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * @author brad.wu
 *
 */
public class EnterpriseTest {
	@Test
	public void testEnterprise() {
		Enterprise e = new Enterprise();
		System a1 = new System("a1");
		System a2 = new System("a2");
		System a3 = new System("a1"); // same id with a1

		// prepare
		e.prepareSystem(a1);
		assertEquals(a1, e.getSystem("a1"));

		// prepare another
		e.prepareSystem(a2);
		assertEquals(2, e.getSystems().size());
		assertEquals(a1, e.getSystem("a1"));
		assertEquals(0, e.getStartedSystems().size());

		// single startup
		e.startupSystem("a1");
		assertEquals(1, e.getStartedSystems().size());
		assertEquals(a1, e.getStartedSystems().iterator().next());

		// startup all
		e.startup();
		assertEquals(2, e.getStartedSystems().size());

		// single shutdown
		e.shutdownSystem("a2");
		assertEquals(1, e.getStartedSystems().size());
		assertEquals(a1, e.getStartedSystems().iterator().next());

		// shutdown all
		e.shutdown();
		assertEquals(0, e.getStartedSystems().size());
		assertEquals(2, e.getSystems().size());

		// startup with duplicated id
		e.startupSystem(a1);
		e.startupSystem(a2);
		assertEquals(2, e.getStartedSystems().size());
		e.startupSystem(a3);
		assertEquals(2, e.getStartedSystems().size());
		List<ISystem> started = new ArrayList<ISystem>(e.getStartedSystems());
		Collections.sort(started, new Comparator<ISystem>() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(ISystem o1, ISystem o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		assertEquals(a3, started.get(0));
		assertEquals(a2, started.get(1));

		// prepare with duplicate id, and shutdown the existed one
		e.prepareSystem(a1);
		assertEquals(2, e.getSystems().size());
		assertEquals(1, e.getStartedSystems().size());
		assertEquals(a2, e.getStartedSystems().iterator().next());

		// prepare with duplicated id, same and difference instance
		e.prepareSystem(a1);
		e.prepareSystem(a3);
		assertEquals(2, e.getSystems().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongPrepare() {
		Enterprise e = new Enterprise();
		e.prepareSystem(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStartup1() {
		Enterprise e = new Enterprise();
		e.startupSystem("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStartup2() {
		Enterprise e = new Enterprise();
		e.startupSystem((String) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStartup3() {
		Enterprise e = new Enterprise();
		e.startupSystem((ISystem) null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void testWrongStartup4() {
		Enterprise e = new Enterprise();
		e.startupSystem("a1");
	}

	public void testWroingGet() {
		Enterprise e = new Enterprise();
		System a1 = new System("a1");
		System a2 = new System("a2");

		e.prepareSystem(a1);
		e.prepareSystem(a2);

		assertNull(e.getSystem("a3"));
	}

	@Test
	public void testFindResource() {
		System system = new System("system");
		Application shop = new Application("shop");
		Component toySaler = new Component("toySaler");
		toySaler.setContainer(shop);
		Resource tedBear = new Resource("tedBear");
		ResourceUtils.registerResource(system, shop);
		ResourceUtils.registerResource(shop, toySaler);
		ResourceUtils.registerResource(toySaler, tedBear);

		Enterprise e = new Enterprise();
		e.startupSystem(system);

		assertEquals(shop, e.findResource("system/shop"));
		assertEquals(toySaler, e.findResource("/system/shop/toySaler"));
		assertEquals(tedBear, e.findResource("system/shop/toySaler/tedBear/"));
	}

	@Test(expected = SystemNotStartException.class)
	public void testFindResourceNotStarted() {
		System shop = new System("shop");

		Enterprise e = new Enterprise();
		e.prepareSystem(shop);

		assertEquals(shop, e.findResource("shop"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindResourceIncorrectId() {
		Enterprise e = new Enterprise();
		e.findResource("");
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}
}

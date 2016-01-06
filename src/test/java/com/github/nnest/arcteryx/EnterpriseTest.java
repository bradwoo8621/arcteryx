/**
 * 
 */
package com.github.nnest.arcteryx;

import static org.junit.Assert.assertEquals;

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
		Application a1 = new Application("a1");
		Application a2 = new Application("a2");
		Application a3 = new Application("a1"); // same id with a1

		// prepare
		e.prepareApplication(a1);
		assertEquals(a1, e.getApplication("a1"));

		// prepare another
		e.prepareApplication(a2);
		assertEquals(2, e.getApplications().size());
		assertEquals(a1, e.getApplication("a1"));
		assertEquals(0, e.getStartedApplications().size());

		// single startup
		e.startupApplication("a1");
		assertEquals(1, e.getStartedApplications().size());
		assertEquals(a1, e.getStartedApplications().iterator().next());

		// startup all
		e.startup();
		assertEquals(2, e.getStartedApplications().size());

		// single shutdown
		e.shutdownApplication("a2");
		assertEquals(1, e.getStartedApplications().size());
		assertEquals(a1, e.getStartedApplications().iterator().next());

		// shutdown all
		e.shutdown();
		assertEquals(0, e.getStartedApplications().size());
		assertEquals(2, e.getApplications().size());

		// startup with duplicated id
		e.startupApplication(a1);
		e.startupApplication(a2);
		assertEquals(2, e.getStartedApplications().size());
		e.startupApplication(a3);
		assertEquals(2, e.getStartedApplications().size());
		List<IApplication> started = new ArrayList<IApplication>(e.getStartedApplications());
		Collections.sort(started, new Comparator<IApplication>() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(IApplication o1, IApplication o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		assertEquals(a3, started.get(0));
		assertEquals(a2, started.get(1));

		// prepare with duplicate id, and shutdown the existed one
		e.prepareApplication(a1);
		assertEquals(2, e.getApplications().size());
		assertEquals(1, e.getStartedApplications().size());
		assertEquals(a2, e.getStartedApplications().iterator().next());

		// prepare with duplicated id, same and difference instance
		e.prepareApplication(a1);
		e.prepareApplication(a3);
		assertEquals(2, e.getApplications().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongPrepare() {
		Enterprise e = new Enterprise();
		e.prepareApplication(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStartup1() {
		Enterprise e = new Enterprise();
		e.startupApplication("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStartup2() {
		Enterprise e = new Enterprise();
		e.startupApplication((String) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongStartup3() {
		Enterprise e = new Enterprise();
		e.startupApplication((IApplication) null);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void testWrongStartup4() {
		Enterprise e = new Enterprise();
		e.startupApplication("a1");
	}

	@Test(expected = ResourceNotFoundException.class)
	public void testWroingGet() {
		Enterprise e = new Enterprise();
		Application a1 = new Application("a1");
		Application a2 = new Application("a2");

		e.prepareApplication(a1);
		e.prepareApplication(a2);

		e.getApplication("a3");
	}
}

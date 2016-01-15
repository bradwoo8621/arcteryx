/**
 * 
 */
package com.github.nnest.arcteryx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author brad.wu
 *
 */
public class ResourceFinderTest {
	@Test
	public void testOneApplication() {
		Application shop = new Application("shop");
		Component toySaler = new Component("toySaler");
		toySaler.setContainer(shop);
		Resource tedBear = new Resource("tedBear");
		ResourceUtils.registerResource(shop, toySaler);
		ResourceUtils.registerResource(toySaler, tedBear);

		assertEquals(toySaler, shop.findResource("toySaler"));
		assertEquals(tedBear, shop.findResource("toySaler/tedBear"));
	}

	@Test
	public void testOnApplicationNotFound() {
		Application shop = new Application("shop");
		Component toySaler = new Component("toySaler");
		toySaler.setContainer(shop);
		Resource tedBear = new Resource("tedBear");
		ResourceUtils.registerResource(shop, toySaler);
		ResourceUtils.registerResource(toySaler, tedBear);

		assertNull(shop.findResource("teaSaler"));
		assertNull(shop.findResource("teaSaler/blackTea"));
		assertNull(shop.findResource("toySaler/tedBear/ironMan"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyId() {
		Application shop = new Application("shop");
		shop.findResource("");
	}

	private static class Resource extends AbstractResource {
		public Resource(String id) {
			super(id);
		}
	}
}

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

	@Test
	public void testAppOverride() {
		Application shop = new Application("shop");
		Component toySaler = new Component("toySaler");
		toySaler.setContainer(shop);
		Resource tedBear = new Resource("tedBear");
		ResourceUtils.registerResource(shop, toySaler);
		ResourceUtils.registerResource(toySaler, tedBear);

		Application newShop = new Application("shop");
		ResourceUtils.registerResource(shop, newShop);

		assertEquals(toySaler, shop.findResource("toySaler"));
		assertEquals(tedBear, shop.findResource("toySaler/tedBear"));

		Component newToySaler = new Component("toySaler");
		ResourceUtils.registerResource(newShop, newToySaler);

		assertEquals(newToySaler, shop.findResource("toySaler"));
		assertEquals(tedBear, shop.findResource("toySaler/tedBear"));

		Component teaSaler = new Component("teaSaler");
		ResourceUtils.registerResource(newShop, teaSaler);

		assertEquals(teaSaler, shop.findResource("teaSaler"));

		ResourceUtils.registerResource(newToySaler, tedBear);

		assertEquals(tedBear, shop.findResource("toySaler/tedBear"));

		Application newerShop = new Application("shop");
		ResourceUtils.registerResource(newShop, newerShop);

		assertEquals(newToySaler, shop.findResource("toySaler"));
		assertEquals(teaSaler, shop.findResource("teaSaler"));
		assertEquals(tedBear, shop.findResource("toySaler/tedBear"));

		ResourceUtils.registerResource(newerShop, toySaler);
		
		assertEquals(toySaler, shop.findResource("toySaler"));
		
		Component legoSaler = new Component("legoSaler");
		ResourceUtils.registerResource(newToySaler, legoSaler);
		Resource starWar = new Resource("starWar");
		ResourceUtils.registerResource(legoSaler, starWar);
		
		assertEquals(starWar, shop.findResource("toySaler/legoSaler/starWar"));
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

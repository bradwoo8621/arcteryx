/**
 * 
 */
package com.github.nnest.arcteryx;

import org.junit.Test;

/**
 * @author brad.wu
 *
 */
public class ComponentTest {
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterAppAsChild() {
		Component comp = new Component("comp");
		Application app = new Application("app");
		ResourceUtils.registerResource(comp, app);
	}
}

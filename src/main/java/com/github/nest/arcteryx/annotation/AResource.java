/**
 * 
 */
package com.github.nest.arcteryx.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A resource.<br/>
 * Class or method which annotated by this should be treated as a resource.
 * 
 * @author brad.wu
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface AResource {
	/**
	 * get resource id
	 * 
	 * @return
	 */
	String reosurceId();

	/**
	 * get component id
	 * 
	 * @return
	 */
	String[] componentId() default {};
}

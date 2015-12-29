/**
 * 
 */
package com.github.nest.arcteryx.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A component.<br/>
 * Class which annotated by this should be treated as a component.
 * 
 * @author brad.wu
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface AComponent {
	/**
	 * get id
	 * 
	 * @return
	 */
	String id();

	/**
	 * get application id
	 * 
	 * @return
	 */
	String applicationId() default "";
}

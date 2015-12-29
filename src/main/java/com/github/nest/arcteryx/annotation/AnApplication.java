/**
 * 
 */
package com.github.nest.arcteryx.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An application.<br/>
 * Class which annotated by this should be treated as an application.
 * 
 * @author brad.wu
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface AnApplication {
	/**
	 * get id
	 * 
	 * @return
	 */
	String id();
}

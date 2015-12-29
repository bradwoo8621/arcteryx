/**
 * 
 */
package com.github.nest.arcteryx.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A resource lifecycle listener
 * 
 * @author brad.wu
 */
@Target(TYPE)
@Retention(RUNTIME)
@AResourceListener
public @interface AComponentRegistrationListener {
	/**
	 * get application id
	 * 
	 * @return
	 */
	String applicationId();
}

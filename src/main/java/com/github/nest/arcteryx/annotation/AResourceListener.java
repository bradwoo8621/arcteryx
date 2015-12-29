/**
 * 
 */
package com.github.nest.arcteryx.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A resource listener
 * 
 * @author brad.wu
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface AResourceListener {
}

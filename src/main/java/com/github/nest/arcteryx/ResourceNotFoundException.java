/**
 * 
 */
package com.github.nest.arcteryx;

/**
 * Resource not found exception
 * 
 * @author brad.wu
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4212494059714396306L;

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}

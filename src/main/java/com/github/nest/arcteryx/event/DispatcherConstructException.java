/**
 * 
 */
package com.github.nest.arcteryx.event;

/**
 * Resource event dispatcher construct exception
 * 
 * @author brad.wu
 */
public class DispatcherConstructException extends RuntimeException {
	private static final long serialVersionUID = -4294088199218988981L;

	public DispatcherConstructException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DispatcherConstructException(String message, Throwable cause) {
		super(message, cause);
	}

	public DispatcherConstructException(String message) {
		super(message);
	}
}

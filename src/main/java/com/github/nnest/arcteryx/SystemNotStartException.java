/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Application not start exception
 * 
 * @author brad.wu
 */
public class SystemNotStartException extends RuntimeException {
	private static final long serialVersionUID = -8908580663301078806L;

	public SystemNotStartException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SystemNotStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemNotStartException(String message) {
		super(message);
	}
}

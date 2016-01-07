/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Application not start exception
 * 
 * @author brad.wu
 */
public class ApplicationNotStartException extends RuntimeException {
	private static final long serialVersionUID = -8908580663301078806L;

	public ApplicationNotStartException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationNotStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationNotStartException(String message) {
		super(message);
	}
}

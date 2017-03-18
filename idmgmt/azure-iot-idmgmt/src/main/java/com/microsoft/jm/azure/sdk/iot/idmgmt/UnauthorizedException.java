/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt;

/**
 * @author jurgenma
 *
 */
public class UnauthorizedException extends DeviceIdentityManagementException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UnauthorizedException() {
	}

	/**
	 * @param message
	 */
	public UnauthorizedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnauthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

/**
 * 
 */
package com.microsoft.azure.sdk.iot.idmgmt;

/**
 * @author jurgenma
 *
 */
public class DeviceIdentityManagementException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DeviceIdentityManagementException() {
	}

	/**
	 * @param message
	 */
	public DeviceIdentityManagementException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DeviceIdentityManagementException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DeviceIdentityManagementException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DeviceIdentityManagementException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

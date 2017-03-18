/**
 * 
 */
package com.microsoft.azure.sdk.iot.idmgmt;

/**
 * @author jurgenma
 *
 */
public final class DeviceAuthentication {

	private DeviceKeyInfo symmetricKey;
	
	private DeviceX509Thumbprint x509Thumbprint;
	
	/**
	 * 
	 */
	public DeviceAuthentication() {
	}

	/**
	 * @return the symmetricKey
	 */
	public final DeviceKeyInfo getSymmetricKey() {
		return symmetricKey;
	}

	/**
	 * @param symmetricKey the symmetricKey to set
	 */
	public final void setSymmetricKey(DeviceKeyInfo symmetricKey) {
		this.symmetricKey = symmetricKey;
	}

	/**
	 * @return the x509Thumbprint
	 */
	public final DeviceX509Thumbprint getX509Thumbprint() {
		return x509Thumbprint;
	}

	/**
	 * @param x509Thumbprint the x509Thumbprint to set
	 */
	public final void setX509Thumbprint(DeviceX509Thumbprint x509Thumbprint) {
		this.x509Thumbprint = x509Thumbprint;
	}

}

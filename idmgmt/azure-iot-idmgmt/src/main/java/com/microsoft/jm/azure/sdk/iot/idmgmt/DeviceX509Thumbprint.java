/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt;

/**
 * @author jurgenma
 *
 */
public final class DeviceX509Thumbprint {

	private String primaryThumbprint;
	
	private String secondaryThumbprint;
	
	/**
	 * 
	 */
	public DeviceX509Thumbprint() {
	}

	/**
	 * @return the primaryThumbprint
	 */
	public final String getPrimaryThumbprint() {
		return primaryThumbprint;
	}

	/**
	 * @param primaryThumbprint the primaryThumbprint to set
	 */
	public final void setPrimaryThumbprint(String primaryThumbprint) {
		this.primaryThumbprint = primaryThumbprint;
	}

	/**
	 * @return the secondaryThumbprint
	 */
	public final String getSecondaryThumbprint() {
		return secondaryThumbprint;
	}

	/**
	 * @param secondaryThumbprint the secondaryThumbprint to set
	 */
	public final void setSecondaryThumbprint(String secondaryThumbprint) {
		this.secondaryThumbprint = secondaryThumbprint;
	}

}

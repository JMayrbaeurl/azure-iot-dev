/**
 * 
 */
package com.microsoft.azure.sdk.iot.idmgmt;

/**
 * @author jurgenma
 *
 */
public final class DeviceKeyInfo {

	private String primaryKey;
	private String secondaryKey;
	
	/**
	 * 
	 */
	public DeviceKeyInfo() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the primaryKey
	 */
	public final String getPrimaryKey() {
		return primaryKey;
	}
	/**
	 * @param primaryKey the primaryKey to set
	 */
	public final void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	/**
	 * @return the secondaryKey
	 */
	public final String getSecondaryKey() {
		return secondaryKey;
	}
	/**
	 * @param secondaryKey the secondaryKey to set
	 */
	public final void setSecondaryKey(String secondaryKey) {
		this.secondaryKey = secondaryKey;
	}

}

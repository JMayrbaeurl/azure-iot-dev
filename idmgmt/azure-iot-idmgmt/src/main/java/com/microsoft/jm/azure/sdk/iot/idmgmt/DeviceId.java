/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt;

import java.util.Date;

/**
 * @author jurgenma
 *
 */
public final class DeviceId {

	private String deviceId;
	
	private String generationId;
	
	private String etag;
	
	private String connectionState;
	
	private String status;
	
	private String statusReason;
	
	private Date connectionStateUpdatedTime;
	
	private Date statusUpdatedTime;
	
	private Date lastActivityTime;
	
	private long cloudToDeviceMessageCount;
	
	private DeviceAuthentication authentication;
	
	/**
	 *
	 */
	public DeviceId(String id) {
		this.deviceId = id;
	}
	
	public DeviceId() {
	}

	/**
	 * @return the deviceId
	 */
	public final String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public final void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the generationId
	 */
	public final String getGenerationId() {
		return generationId;
	}

	/**
	 * @param generationId the generationId to set
	 */
	public final void setGenerationId(String generationId) {
		this.generationId = generationId;
	}

	/**
	 * @return the etag
	 */
	public final String getEtag() {
		return etag;
	}

	/**
	 * @param etag the etag to set
	 */
	public final void setEtag(String etag) {
		this.etag = etag;
	}

	/**
	 * @return the connectionState
	 */
	public final String getConnectionState() {
		return connectionState;
	}

	/**
	 * @param connectionState the connectionState to set
	 */
	public final void setConnectionState(String connectionState) {
		this.connectionState = connectionState;
	}

	/**
	 * @return the status
	 */
	public final String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public final void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the statusReason
	 */
	public final String getStatusReason() {
		return statusReason;
	}

	/**
	 * @param statusReason the statusReason to set
	 */
	public final void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	/**
	 * @return the connectionStateUpdatedTime
	 */
	public final Date getConnectionStateUpdatedTime() {
		return connectionStateUpdatedTime;
	}

	/**
	 * @param connectionStateUpdatedTime the connectionStateUpdatedTime to set
	 */
	public final void setConnectionStateUpdatedTime(Date connectionStateUpdatedTime) {
		this.connectionStateUpdatedTime = connectionStateUpdatedTime;
	}

	/**
	 * @return the statusUpdatedTime
	 */
	public final Date getStatusUpdatedTime() {
		return statusUpdatedTime;
	}

	/**
	 * @param statusUpdatedTime the statusUpdatedTime to set
	 */
	public final void setStatusUpdatedTime(Date statusUpdatedTime) {
		this.statusUpdatedTime = statusUpdatedTime;
	}

	/**
	 * @return the lastActivityTime
	 */
	public final Date getLastActivityTime() {
		return lastActivityTime;
	}

	/**
	 * @param lastActivityTime the lastActivityTime to set
	 */
	public final void setLastActivityTime(Date lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}

	/**
	 * @return the cloudToDeviceMessageCount
	 */
	public final long getCloudToDeviceMessageCount() {
		return cloudToDeviceMessageCount;
	}

	/**
	 * @return the authentication
	 */
	public final DeviceAuthentication getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public final void setAuthentication(DeviceAuthentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * @param cloudToDeviceMessageCount the cloudToDeviceMessageCount to set
	 */
	public final void setCloudToDeviceMessageCount(long cloudToDeviceMessageCount) {
		this.cloudToDeviceMessageCount = cloudToDeviceMessageCount;
	}
}

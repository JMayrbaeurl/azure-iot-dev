/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt;

import java.util.List;

/**
 * @author jurgenma
 *
 */
public interface DeviceIdentityManagement {
	
	public static final String IOTHUB_DOMAINNAME = "azure-devices.net";
	
	public DeviceId createDeviceIdentity(String deviceId);
	public void createDevices(List<String> ids);
	
	public List<DeviceId> getDevices();
}

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
	public DeviceId createDeviceIdentity(DeviceId deviceId);
	public void createDevices(List<String> ids);
	
	public void deleteDeviceIdentity(String deviceId);
	public void deleteDeviceIdentity(DeviceId deviceId);
	
	public DeviceId getDevice(String deviceId);
	public List<DeviceId> getDevices();
}

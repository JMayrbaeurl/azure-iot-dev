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
	
	public void createDeviceIdentity();
	
	public List<DeviceId> getDevices();
}

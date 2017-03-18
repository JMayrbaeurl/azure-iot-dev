/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.simple.device;

/**
 * @author jurgenma
 *
 */
public final class DeviceClient {

	public void sendMessage(final Message message) {
		
		if (message == null)
			throw new IllegalArgumentException("Parameter message must not be null");
		
		
	}
	
}

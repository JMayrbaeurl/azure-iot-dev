package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceId;
import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement;

public class TestRESTBasedDeviceIdMgmtImpl {

	@Test
	public void testGetDevices() {
		
		RESTBasedDeviceIdMgmtImpl idManager = new RESTBasedDeviceIdMgmtImpl("jmiottesting",  
				"iothubowner", "MJ05RIwNsw6WAX5gg03UH4E0Tk7HuIxGqtd/OukePoU=");
		List<DeviceId> devices = idManager.getDevices();
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(devices);
	}
	
	@Test
	public void testHttpClientCreationOnConstruction() throws IOException {
	
		RESTBasedDeviceIdMgmtImpl impl = new RESTBasedDeviceIdMgmtImpl("testhub", "", "");
		assertNotNull(impl);
		assertNotNull(impl.getHttpClient());
		
		impl.close();
	}
	
	@Test
	public void testCreateBasicIoTHubURI() throws IOException {
		
		RESTBasedDeviceIdMgmtImpl impl = new RESTBasedDeviceIdMgmtImpl("testhub", "", "");
		assertNotNull(impl);
		assertEquals("testhub." + DeviceIdentityManagement.IOTHUB_DOMAINNAME, impl.createBasicIoTHubURI());
		
		impl.close();
	}

	@Test
	public void testCreateBasicIoTHubURL() throws IOException {
		
		RESTBasedDeviceIdMgmtImpl impl = new RESTBasedDeviceIdMgmtImpl("testhub", "", "");
		assertNotNull(impl);
		assertEquals("https://testhub." + DeviceIdentityManagement.IOTHUB_DOMAINNAME + "/", 
				impl.createBasicIoTHubURL());
		
		impl.close();
		
	}
}

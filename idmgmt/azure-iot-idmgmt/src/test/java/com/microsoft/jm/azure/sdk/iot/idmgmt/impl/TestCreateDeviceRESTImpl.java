package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceId;

public class TestCreateDeviceRESTImpl extends AbstractIoTHubAccessingTest {

	public TestCreateDeviceRESTImpl() {
	}

	@Test
	public void testCreateDevice() {
		
		Assume.assumeTrue("Test skipped. No valid Iot Hub configuration available", this.hasValidIotHubConfiguration());
		
		RESTBasedDeviceIdMgmtImpl idManager = this.createInstance();
		DeviceId result = idManager.createDeviceIdentity("createdTestDeviceId");
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		Assert.assertNotNull(result);
	}
}

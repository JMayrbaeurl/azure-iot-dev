package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

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
		try {
			DeviceId result = idManager.createDeviceIdentity(this.testDeviceId);
			try {
				Assert.assertNotNull(result);
			} finally {
				idManager.deleteDeviceIdentity(this.testDeviceId);
			}
		} finally { this.closeInstanceQuietly(idManager);}
	}
}

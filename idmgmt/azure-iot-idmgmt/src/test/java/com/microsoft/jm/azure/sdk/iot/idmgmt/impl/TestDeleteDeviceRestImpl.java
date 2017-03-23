package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import org.junit.Assume;
import org.junit.Test;

public class TestDeleteDeviceRestImpl extends AbstractIoTHubAccessingTest {

	public TestDeleteDeviceRestImpl() {}

	@Test
	public void testDeleteDevice() {
		
		Assume.assumeTrue("Test skipped. No valid Iot Hub configuration available", this.hasValidIotHubConfiguration());
		
		RESTBasedDeviceIdMgmtImpl idManager = this.createInstance();
		try {
			idManager.createDeviceIdentity(this.testDeviceId);
			idManager.deleteDeviceIdentity(this.testDeviceId);
			
		} finally { this.closeInstanceQuietly(idManager);}
	}
}

package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;

import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceId;

public class TestGetDevicesRESTImpl extends AbstractIoTHubAccessingTest {

	public TestGetDevicesRESTImpl() {
	}

	@Test
	public void testGetDevices() {
		
		Assume.assumeTrue("Test skipped. No valid Iot Hub configuration available", this.hasValidIotHubConfiguration());
		
		RESTBasedDeviceIdMgmtImpl idManager = this.createInstance();
		List<DeviceId> devices = idManager.getDevices();
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(devices);
	}
	
	@Test
	public void testGetDeviceWithId() {
		
		Assume.assumeTrue("Test skipped. No valid Iot Hub configuration available", this.hasValidIotHubConfiguration());
		
		RESTBasedDeviceIdMgmtImpl idManager = this.createInstance();
		DeviceId deviceId = idManager.getDevice(this.idOfExistingDeviceInTestSetup);
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(deviceId);
		assertEquals(this.idOfExistingDeviceInTestSetup, deviceId.getDeviceId());
	}
	
	@Test
	public void testGetNonExistingDeviceWithId() {
		
		Assume.assumeTrue("Test skipped. No valid Iot Hub configuration available", this.hasValidIotHubConfiguration());
		
		RESTBasedDeviceIdMgmtImpl idManager = this.createInstance();
		DeviceId deviceId = idManager.getDevice(this.idOfNonExistingDeviceInTestSetup);
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNull(deviceId);
	}
}

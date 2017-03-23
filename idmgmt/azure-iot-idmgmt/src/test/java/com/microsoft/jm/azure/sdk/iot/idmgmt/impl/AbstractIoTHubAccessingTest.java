package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;

public abstract class AbstractIoTHubAccessingTest {

	protected String iothubname;
	
	protected String iothubPolicykey;
	
	protected String iothubPolicyname;
	
	protected final String testDeviceId = "createdTestDeviceId";
	
	public AbstractIoTHubAccessingTest() {
	}

	@Before
	public void readIoTHubConfig() throws IOException {

		final Properties properties = new Properties();
		
		try (final InputStream stream =
				this.getClass().getClassLoader().getResourceAsStream("iothubconfig.properties")) {
		    properties.load(stream);

			this.iothubname = properties.getProperty("azure.iothub.instance.name");
			this.iothubPolicyname = properties.getProperty("azure.iothub.instance.access.policy");
			this.iothubPolicykey = properties.getProperty("azure.iothub.instance.access.policykey");
		} catch (Exception ex) {
			System.out.println("Could not load iot hub configuration from properties file");
		}
	}
	
	protected RESTBasedDeviceIdMgmtImpl createInstance() {
		
		RESTBasedDeviceIdMgmtImpl idManager = new RESTBasedDeviceIdMgmtImpl(this.iothubname,  
				this.iothubPolicyname, this.iothubPolicykey);
		
		return idManager;
	}
	
	protected boolean hasValidIotHubConfiguration() {
		
		return this.iothubname != null && this.iothubPolicyname != null && this.iothubPolicykey != null;
	}
	
	protected void closeInstanceQuietly(RESTBasedDeviceIdMgmtImpl idManager) {
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

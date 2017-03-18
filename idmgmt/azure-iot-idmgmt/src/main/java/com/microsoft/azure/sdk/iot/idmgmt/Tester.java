package com.microsoft.azure.sdk.iot.idmgmt;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.idmgmt.impl.RESTBasedDeviceIdMgmtImpl;

public class Tester {

	public Tester() {
		// TODO Auto-generated constructor stub 
	}

	public static void main(String[] args) {

		RESTBasedDeviceIdMgmtImpl idManager = new RESTBasedDeviceIdMgmtImpl("jmiottesting",  
				"iothubowner", "MJ05RIwNsw6WAX5gg03UH4E0Tk7HuIxGqtd/OukePoU=");
		idManager.getDevices();
		
		try {
			idManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

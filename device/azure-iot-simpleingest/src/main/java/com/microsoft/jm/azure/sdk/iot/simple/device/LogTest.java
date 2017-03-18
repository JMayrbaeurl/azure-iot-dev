package com.microsoft.jm.azure.sdk.iot.simple.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		final Logger logger = LoggerFactory.getLogger(LogTest.class);
		
		logger.debug("test", new IllegalArgumentException(new IllegalStateException("Just kidding")));
	}

}

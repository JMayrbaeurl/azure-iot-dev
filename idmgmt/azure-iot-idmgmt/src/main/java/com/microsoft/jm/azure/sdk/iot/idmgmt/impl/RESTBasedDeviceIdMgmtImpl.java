/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceId;
import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentitiesRESTApi;
import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement;
import com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagementException;
import com.microsoft.jm.azure.sdk.iot.idmgmt.UnauthorizedException;

/**
 * @author jurgenma
 *
 */
public class RESTBasedDeviceIdMgmtImpl implements DeviceIdentityManagement, Closeable {

	private String apiVersion = DeviceIdentitiesRESTApi.API_VERSION;
	
	private String iothubName;
	
	private String sharedAccessPolicyname = "iothubowner";
	
	private String sharedAccessPolickey;
	
	private long tokenValidTimeInSeconds = 60 * 5;
	
	private HttpClient httpClient;
	
	private ObjectMapper jsonObjectMapper;
	
	protected final Logger logger = LoggerFactory.getLogger(RESTBasedDeviceIdMgmtImpl.class);
	
	/**
	 * 
	 */
	public RESTBasedDeviceIdMgmtImpl() {
		
		this.httpClient = HttpClients.createDefault();
	}
	
	/**
	 * @param hubname
	 */
	public RESTBasedDeviceIdMgmtImpl(final String hubname, String policyname, String policykey) {
		
		this();
		
		this.iothubName = hubname;
		this.sharedAccessPolicyname = policyname;
		this.sharedAccessPolickey = policykey;
	}

	/* (non-Javadoc)
	 * @see com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement#createDeviceIdentity()
	 */
	@Override
	public void createDeviceIdentity() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement#GetDevices()
	 */
	@Override
	public List<DeviceId> getDevices() {
		
		logger.trace("Now calling 'getDevices' on IoT Hub for {}", this.iothubName);
		
		this.checkSetup();
		
		List<DeviceId> result = new ArrayList<DeviceId>();
		
		HttpResponse response = null;
		try {
			response = this.httpClient.execute(this.createHttpRequest(HttpGet.METHOD_NAME,
					DeviceIdentitiesRESTApi.GETDEVICES_COMMAND));
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {				
				ObjectMapper mapper = this.createJsonObjectMapper();

				List<DeviceId> list = mapper.readValue(response.getEntity().getContent(), 
						new TypeReference<List<DeviceId>>(){});
				
				if (list != null) {
					logger.debug("IoT Hub returned a list of {} devices for {}", list.size(), this.iothubName);
					if (list.size() > 0)
						logger.debug("First device has id {}", list.get(0).getDeviceId());
					
					result = list;
				}
			} else {
				this.handleNonOKStatusCode(response);
			}
		} catch(Exception ex) {
			logger.error("Exception on trying to get all device ids from IoT Hub", ex);
		
			 if (!(ex instanceof DeviceIdentityManagementException))
				 throw new RuntimeException(ex);
		} finally {
			this.closeHttpResponseQuietly(response);
		}
		
		return result;
	}
	
	/**
	 * @param response
	 */
	private void closeHttpResponseQuietly(final HttpResponse response) {

		if (response != null) {
			try {
				try {
					EntityUtils.consume(response.getEntity());
				} finally {
					if (response instanceof Closeable)
						((Closeable)response).close();
				}
			} catch (final IOException ignore) {}
		}
	}

	/**
	 * @param response
	 */
	protected void handleNonOKStatusCode(final HttpResponse response) {
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
			throw new UnauthorizedException();
		} else {
			throw new DeviceIdentityManagementException("REST API call with error code '"
					+ response.getStatusLine().getStatusCode() + "' and messsage '"
					+ response.getStatusLine().getReasonPhrase() + "'");
		}
	}

	/**
	 * @param restCommand
	 * @return
	 */
	protected HttpUriRequest createHttpRequest(final String methodName, final String restCommand) {
	
		String fullURL = this.createBasicIoTHubURL() + restCommand + "?api-version=" 
				+ DeviceIdentitiesRESTApi.API_VERSION;
		
		HttpUriRequest request = null;
		if (HttpGet.METHOD_NAME.equals(methodName))
			request = new HttpGet(fullURL);
		else if (HttpPut.METHOD_NAME.equals(methodName))
			request = new HttpPut(fullURL);
		else if (HttpDelete.METHOD_NAME.equals(methodName))
			request = new HttpDelete(fullURL);
		else if (HttpPost.METHOD_NAME.equals(methodName))
			request = new HttpPost(fullURL);
		
		if (request == null)
			throw new IllegalArgumentException("createHttpRequest called with illegal value '"
					+ methodName + "' for parameter methodName");
		
		request.setHeader("authorization", this.createSharedAccessSignature());

		return request;
	}
	
	/**
	 * @return
	 */
	protected String createBasicIoTHubURL() {
		
		return "https://" + this.createBasicIoTHubURI() + "/";
	}
	
	protected String createBasicIoTHubURI() {
		
		return this.iothubName + "." + DeviceIdentityManagement.IOTHUB_DOMAINNAME;
	}
	
	/**
	 * @return
	 */
	protected String createSharedAccessSignature() {
		
		long expiryTime = this.calculateNextExpiryTime(this.tokenValidTimeInSeconds);
		
		String signature = new SASCreator(this.createBasicIoTHubURI(), expiryTime, 
				this.sharedAccessPolickey).createSharedAccessSignature();
		
		return "SharedAccessSignature sr=" + this.iothubName + "." + DeviceIdentityManagement.IOTHUB_DOMAINNAME +
				"&sig=" + signature + "&se=" + expiryTime + 
				"&skn=" + this.sharedAccessPolicyname;
	}
	
	protected long calculateNextExpiryTime(long validSeconds) {
		
        Date now = new Date();
        Date previousDate = new Date(1970);
        return ((now.getTime() - previousDate.getTime()) / 1000) + validSeconds;
	}
	
	/**
	 * @return
	 */
	protected ObjectMapper createJsonObjectMapper() {
		
		ObjectMapper result = this.jsonObjectMapper;
		
		if (result == null) {
			
			result = new ObjectMapper();
			result.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		
		return result;
	}

	/**
	 * 
	 */
	private void checkSetup() {
		
		if (this.iothubName == null) {
			
			logger.error("No IoT Hub name available. Check the configuration setup");
			
			throw new IllegalStateException("No Azure IoT Hub name configured");
		}
		
		if (this.sharedAccessPolicyname == null || this.sharedAccessPolickey == null) {
			
			logger.error("Invalid shared access policy setup. Check the configuration setup");
			
			throw new IllegalStateException("Invalid shared access policy setup. Check the configuration setup");
			
		}
		
		if (this.httpClient == null) {
			
			logger.error("No Http client available. Check the configuration setup");
			
			throw new IllegalStateException("No Http client available. Check the configuration setup");
		}
	}
	
	/**
	 * @return the jsonObjectMapper
	 */
	public final ObjectMapper getJsonObjectMapper() {
		return this.jsonObjectMapper;
	}

	/**
	 * @param jsonObjectMapper the jsonObjectMapper to set
	 */
	public final void setJsonObjectMapper(ObjectMapper jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
	}

	/**
	 * @return the apiVersion
	 */
	public final String getApiVersion() {
		return apiVersion;
	}

	/**
	 * @param apiVersion the apiVersion to set
	 */
	public final void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	/**
	 * @return the iothubName
	 */
	public final String getIothubName() {
		return iothubName;
	}

	/**
	 * @param iothubName the iothubName to set
	 */
	public final void setIothubName(String iothubName) {
		this.iothubName = iothubName;
	}

	/**
	 * @return the sharedAccessPolicyname
	 */
	public final String getSharedAccessPolicyname() {
		return sharedAccessPolicyname;
	}

	/**
	 * @param sharedAccessPolicyname the sharedAccessPolicyname to set
	 */
	public final void setSharedAccessPolicyname(String sharedAccessPolicyname) {
		this.sharedAccessPolicyname = sharedAccessPolicyname;
	}

	@Override
	public void close() throws IOException {
		
		if (this.httpClient != null && this.httpClient instanceof Closeable) {
			((Closeable)this.httpClient).close();
			this.httpClient = null;
		}
	}

}

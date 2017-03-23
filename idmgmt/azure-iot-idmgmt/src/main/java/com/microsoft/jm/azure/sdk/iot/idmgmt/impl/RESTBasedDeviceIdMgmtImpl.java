/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
	
	private int httpTimeoutInMilliseconds = DeviceIdentitiesRESTApi.DEFAULT_HTTP_TIMOUT_MS;
	
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
	 * @see com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement#createDevices(java.util.List)
	 */
	@Override
	public void createDevices(final List<String> stringIds) {

		logger.trace("Now calling 'createDevices' on IoT Hub for {}", this.iothubName);

		if (stringIds == null) {
			
			logger.error("createDevices called with null value for parameter 'stringIds'");

			throw new IllegalArgumentException("createDevices called with null value for parameter 'stringIds'");			
		}
		
		if (stringIds.isEmpty())
			return;
		
		this.checkSetup();
		
		List<DeviceId> ids = new ArrayList<>();
		for(int i = 0; i < stringIds.size(); i++) {
			String idAsString = stringIds.get(i);
			if (StringUtils.isEmpty(idAsString)) {
				
				logger.error("createDevices called with null value for id in item list at index " + i);
				
				throw new IllegalArgumentException("createDevices called with null value for id in item list at index " + i);
			}
			ids.add(new DeviceId(idAsString));
		}
		
		ObjectMapper mapper = this.createJsonObjectMapper();
		
		HttpResponse response = null;
		try {
			String idsString = mapper.writeValueAsString(ids);

			HttpUriRequest request = this.createHttpRequest(HttpPost.METHOD_NAME,
					DeviceIdentitiesRESTApi.CREATEDEVICES_COMMAND);
			((HttpEntityEnclosingRequest)request).setEntity(new StringEntity(idsString,
			        ContentType.create("application/json", Consts.UTF_8)));
			
			response = this.httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
			} else
				this.handleNonOKStatusCode(response);
		} catch (Exception ex) {
			logger.error("Exception on trying to get all device ids from IoT Hub", ex);
			
			 if (!(ex instanceof DeviceIdentityManagementException))
				 throw new RuntimeException(ex);
			 else
				 throw (DeviceIdentityManagementException)ex;
		} finally {
				this.closeHttpResponseQuietly(response);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement#createDeviceIdentity(com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceId)
	 */
	@Override
	public DeviceId createDeviceIdentity(final DeviceId deviceId) {
		
		logger.trace("Now calling 'createDevice' on IoT Hub for {}", this.iothubName);
		
		return this.doCreateDeviceIdentity(deviceId);
	}	
	
	@Override
	public DeviceId createDeviceIdentity(final String deviceId) {
		
		logger.trace("Now calling 'createDevice' on IoT Hub for {}", this.iothubName);

		if (StringUtils.isEmpty(deviceId)) {
			logger.error("createDeviceIdentity called with null value for parameter 'deviceId'");
			
			throw new IllegalArgumentException("createDeviceIdentity called with null value for parameter 'deviceId'");
		}
		
		return this.doCreateDeviceIdentity(new DeviceId(deviceId));
	}
	
	/**
	 * @param deviceId
	 * @return
	 */
	protected DeviceId doCreateDeviceIdentity(final DeviceId id) {
		
		if (id == null || StringUtils.isEmpty(id.getDeviceId())) {
			logger.error("createDeviceIdentity called with null value for deviceId");
			
			throw new IllegalArgumentException("createDeviceIdentity called with null value for deviceId");
		}
		
		this.checkSetup();

		DeviceId result = null;
		ObjectMapper mapper = this.createJsonObjectMapper();
		
		HttpResponse response = null;
		try {
			String idString = mapper.writeValueAsString(id);

			HttpUriRequest request = this.createHttpRequest(HttpPut.METHOD_NAME,
					DeviceIdentitiesRESTApi.CREATEDEVICE_COMMAND + "/" + id.getDeviceId());
			((HttpEntityEnclosingRequest)request).setEntity(new StringEntity(idString,
			        ContentType.create("application/json", Consts.UTF_8)));
			
			response = this.httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = mapper.readValue(response.getEntity().getContent(), new TypeReference<DeviceId>() {});
			} else
				this.handleNonOKStatusCode(response);
		} catch (Exception ex) {
			logger.error("Exception on trying to create device id '"+ id.getDeviceId() + "' on IoT Hub", ex);
			
			 if (!(ex instanceof DeviceIdentityManagementException))
				 throw new RuntimeException(ex);
			 else
				 throw (DeviceIdentityManagementException)ex;
		} finally {
				this.closeHttpResponseQuietly(response);
		}
		
		return result;
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
	

	/* (non-Javadoc)
	 * @see com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement#deleteDeviceIdentity(java.lang.String)
	 */
	@Override
	public void deleteDeviceIdentity(final String deviceId) {
		
		logger.trace("Now calling 'deleteDevice' on IoT Hub for {}", this.iothubName);

		if (StringUtils.isEmpty(deviceId)) {
			logger.error("deleteDeviceIdentity called with null value for parameter 'deviceId'");
			
			throw new IllegalArgumentException("deleteDeviceIdentity called with null value for parameter 'deviceId'");
		}
		
		this.doDeleteDeviceIdentity(new DeviceId(deviceId));
	}

	/* (non-Javadoc)
	 * @see com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceIdentityManagement#deleteDeviceIdentity(com.microsoft.jm.azure.sdk.iot.idmgmt.DeviceId)
	 */
	@Override
	public void deleteDeviceIdentity(final DeviceId deviceId) {
		
		logger.trace("Now calling 'deleteDevice' on IoT Hub for {}", this.iothubName);
		
		this.doDeleteDeviceIdentity(deviceId);
	}

	/**
	 * @param deviceId
	 */
	protected void doDeleteDeviceIdentity(final DeviceId id) {
		
		if (id == null || StringUtils.isEmpty(id.getDeviceId())) {
			logger.error("deleteDeviceIdentity called with null value for deviceId");
			
			throw new IllegalArgumentException("deleteDeviceIdentity called with null value for deviceId");
		}
		
		this.checkSetup();

		HttpResponse response = null;
		try {
			HttpUriRequest request = this.createHttpRequest(HttpDelete.METHOD_NAME,
					DeviceIdentitiesRESTApi.DELETEDEVICE_COMMAND + "/" + id.getDeviceId());
			request.setHeader("If-Match", "*");
			
			response = this.httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK 
					&& response.getStatusLine().getStatusCode() != 204)
				this.handleNonOKStatusCode(response);
		} catch (Exception ex) {
			logger.error("Exception on trying to delete device id '"+ id.getDeviceId() + "' on IoT Hub", ex);
			
			 if (!(ex instanceof DeviceIdentityManagementException))
				 throw new RuntimeException(ex);
			 else
				 throw (DeviceIdentityManagementException)ex;
		} finally {
				this.closeHttpResponseQuietly(response);
		}
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
	
		String fullURL = this.createBasicIoTHubURL() + restCommand + "?api-version=" + this.apiVersion;
		
		HttpRequestBase request = null;
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
		request.setHeader("Request-Id", "1001");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("charset", "utf-8");
        
        RequestConfig requestConfig = RequestConfig.custom()
        		  .setSocketTimeout(this.httpTimeoutInMilliseconds)
        		  .setConnectTimeout(this.httpTimeoutInMilliseconds)
        		  .setConnectionRequestTimeout(this.httpTimeoutInMilliseconds)
        		  .build();

        request.setConfig(requestConfig);

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
	 * @return the httpClient
	 */
	public final HttpClient getHttpClient() {
		return httpClient;
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

	/**
	 * @return the httpTimeoutInMilliseconds
	 */
	public final int getHttpTimeoutInMilliseconds() {
		return httpTimeoutInMilliseconds;
	}

	/**
	 * @param httpTimeoutInMilliseconds the httpTimeoutInMilliseconds to set
	 */
	public final void setHttpTimeoutInMilliseconds(int httpTimeoutInMilliseconds) {
		this.httpTimeoutInMilliseconds = httpTimeoutInMilliseconds;
	}

	@Override
	public void close() throws IOException {
		
		if (this.httpClient != null && this.httpClient instanceof Closeable) {
			((Closeable)this.httpClient).close();
			this.httpClient = null;
		}
	}

}

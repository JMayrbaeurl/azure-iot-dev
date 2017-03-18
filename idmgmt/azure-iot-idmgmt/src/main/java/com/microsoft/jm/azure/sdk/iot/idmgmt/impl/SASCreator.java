/**
 * 
 */
package com.microsoft.jm.azure.sdk.iot.idmgmt.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author jurgenma
 *
 */
public class SASCreator {

	private final String resourceUri;
	
	private long expiryTime;
	
	private final String policyKey;

	public SASCreator(String resourceUri, long expiryTime, String policyKey) {
		
		super();
		
		this.resourceUri = resourceUri;
		this.expiryTime = expiryTime;
		this.policyKey = policyKey;
	}
	
	/**
	 * @return
	 */
	public String createSharedAccessSignature() {
		
		try {
			return SASCreator.getSignature(this.resourceUri, this.expiryTime, this.policyKey);
			
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key exception on creation of shared access signature", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No such algorithm exception on creation of shared access signature", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding exception on creation of shared access signature", e);
		}
	}

	/**
	 * @return the expiryTime
	 */
	public final long getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @param expiryTime the expiryTime to set
	 */
	public final void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}

	/**
	 * @return the resourceUri
	 */
	public final String getResourceUri() {
		return resourceUri;
	}

	/**
	 * @return the policyKey
	 */
	public final String getPolicyKey() {
		return policyKey;
	}
	
	/**
	 * @param resourceUri
	 * @param expiryTime
	 * @param devicePrimaryKey
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private static String getSignature(String resourceUri, long expiryTime, String devicePrimaryKey)
			throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] textToSign = new String(resourceUri + "\n" + expiryTime).getBytes();
		byte[] decodedDeviceKey = Base64.decodeBase64(devicePrimaryKey);
		byte[] signature = encryptHmacSha256(textToSign, decodedDeviceKey);
		byte[] encryptedSignature = Base64.encodeBase64(signature);
		String encryptedSignatureUtf8 = new String(encryptedSignature, StandardCharsets.UTF_8);

		return URLEncoder.encode(encryptedSignatureUtf8, "utf-8");
	}	
	
	/**
	 * @param textToSign
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private static byte[] encryptHmacSha256(byte[] textToSign, byte[] key)
			throws NoSuchAlgorithmException, InvalidKeyException

	{
		SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
		Mac hMacSha256 = Mac.getInstance("HmacSHA256");
		hMacSha256.init(secretKey);
		return hMacSha256.doFinal(textToSign);
	}
}

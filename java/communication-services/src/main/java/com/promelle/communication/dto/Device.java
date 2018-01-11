package com.promelle.communication.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding device information.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class Device extends AbstractAuditDTO {

	private static final long serialVersionUID = 6963269350970891887L;
	private String deviceId;
	private String deviceType;
	private String deviceToken;
	private String userId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}

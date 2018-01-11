package com.promelle.user.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class DeviceFilter extends SearchFilter {
	private static final long serialVersionUID = 7768723451527125650L;
	private String userId;
	private String deviceId;
	private String deviceToken;
	private Integer status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}

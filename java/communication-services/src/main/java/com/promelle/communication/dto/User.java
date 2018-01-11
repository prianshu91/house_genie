package com.promelle.communication.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class User extends AbstractAuditDTO {
	private static final long serialVersionUID = 6963269350970891987L;
	private String name;
	private String email;
	private String mobile;
	private String otp;
	private String username;
	private String organizationName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email != null ? email.toLowerCase() : null;
	}

	public void setEmail(String email) {
		this.email = email != null ? email.toLowerCase() : null;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

}

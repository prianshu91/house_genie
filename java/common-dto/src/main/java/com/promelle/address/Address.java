package com.promelle.address;

import org.apache.commons.lang.StringUtils;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding signup information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class Address extends AbstractAuditDTO {
	private static final long serialVersionUID = -7014364639549246150L;

	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipCode;
	private Double latitude;
	private Double longitude;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = StringUtils.isBlank(zipCode) ? " " : zipCode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}

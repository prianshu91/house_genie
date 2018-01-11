package com.promelle.product.dto;

import com.promelle.address.Address;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderAddress extends Address {
	private static final long serialVersionUID = -6893401802017895828L;
	private String orderId;
	private String userId;
	private String name;
	private String mobile;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}

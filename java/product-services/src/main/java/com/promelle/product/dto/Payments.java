package com.promelle.product.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding payments type information.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class Payments extends AbstractAuditDTO {

	private static final long serialVersionUID = -689340180201795328L;
	private String userId;
	private String stripeCustomerId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStripeCustomerId() {
		return stripeCustomerId;
	}

	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

}

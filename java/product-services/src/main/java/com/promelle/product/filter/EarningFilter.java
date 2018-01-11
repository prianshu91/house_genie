package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class EarningFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269451527572138L;
	private String ownerId;
	private String shipmentId;
	private String redeemStatus;
	private String text;

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getRedeemStatus() {
		return redeemStatus;
	}

	public void setRedeemStatus(String redeemStatus) {
		this.redeemStatus = redeemStatus;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

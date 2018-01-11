package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderItemFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269451527572138L;
	private String userId;
	private String orderId;
	private String productId;
	private String shipmentId;
	private Boolean isShipmentExists;
	private Boolean includeShipmentStatus;
	private String shipmentStatus;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public Boolean getIsShipmentExists() {
		return isShipmentExists;
	}

	public void setIsShipmentExists(Boolean isShipmentExists) {
		this.isShipmentExists = isShipmentExists;
	}

	public Boolean getIncludeShipmentStatus() {
		return includeShipmentStatus;
	}

	public void setIncludeShipmentStatus(Boolean includeShipmentStatus) {
		this.includeShipmentStatus = includeShipmentStatus;
	}

	public String getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

}

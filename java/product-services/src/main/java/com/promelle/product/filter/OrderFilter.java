package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269451527572138L;
	private String userId;
	private String orderStatus;
	private Boolean includeItems;
	private Boolean includeShipments;
	private Boolean includeEarnings;
	private Boolean excludePending;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Boolean getIncludeItems() {
		return includeItems;
	}

	public void setIncludeItems(Boolean includeItems) {
		this.includeItems = includeItems;
	}

	public Boolean getIncludeShipments() {
		return includeShipments;
	}

	public void setIncludeShipments(Boolean includeShipments) {
		this.includeShipments = includeShipments;
	}

	public Boolean getIncludeEarnings() {
		return includeEarnings;
	}

	public void setIncludeEarnings(Boolean includeEarnings) {
		this.includeEarnings = includeEarnings;
	}

	public Boolean getExcludePending() {
		return excludePending;
	}

	public void setExcludePending(Boolean excludePending) {
		this.excludePending = excludePending;
	}

}

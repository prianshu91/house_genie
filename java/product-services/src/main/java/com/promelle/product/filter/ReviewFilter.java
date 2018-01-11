package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ReviewFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269451527572138L;
	private String userId;
	private String productId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}

package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;
/**
 * 
 * @author Kanak Sony
 * @version 1.0
 */
public class FavoriteFilter extends SearchFilter {
	private static final long serialVersionUID = 4270889555032649192L;
	private String productId;
	private String userId;
	private Boolean favourite;
	
	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}

	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

/**
 * 
 */
package com.promelle.product.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * @author Kanak Sony
 * @version 1.0
 *
 */
public class Favorite extends AbstractAuditDTO {
	private static final long serialVersionUID = -9183044211189710200L;
	private String userId;
	private String productId;
	private Boolean favourite;

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

	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}
}

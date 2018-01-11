package com.promelle.product.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Review extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String title;
	private String description;
	private Double rating;
	private Long timestamp;
	private String userId;
	private String userName;
	private String productId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}

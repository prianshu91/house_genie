package com.promelle.product.dto;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ReviewStats extends AbstractDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private Double averageRating;
	private Integer reviewCount;

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

}

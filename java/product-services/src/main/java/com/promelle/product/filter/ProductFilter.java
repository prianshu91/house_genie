package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269451527572138L;
	private String ownerId;
	private String occasion;
	private String category;
	private String length;
	private String type;
	private String size;
	private String color;
	private String bustFit;
	private String heightFit;
	private Boolean isLended;
	private Integer status;
	private Double rentalPriceStart;
	private Double rentalPriceEnd;
	private Boolean isFavourite;
	private String text;
	private String userId;
	private String waist;
	private String sleeve;

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getIsLended() {
		return isLended;
	}

	public void setIsLended(Boolean isLended) {
		this.isLended = isLended;
	}

	public Double getRentalPriceStart() {
		return rentalPriceStart;
	}

	public void setRentalPriceStart(Double rentalPriceStart) {
		this.rentalPriceStart = rentalPriceStart;
	}

	public Double getRentalPriceEnd() {
		return rentalPriceEnd;
	}

	public void setRentalPriceEnd(Double rentalPriceEnd) {
		this.rentalPriceEnd = rentalPriceEnd;
	}

	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBustFit() {
		return bustFit;
	}

	public void setBustFit(String bustFit) {
		this.bustFit = bustFit;
	}

	public String getHeightFit() {
		return heightFit;
	}

	public void setHeightFit(String heightFit) {
		this.heightFit = heightFit;
	}

	public String getWaist() {
		return waist;
	}

	public void setWaist(String waist) {
		this.waist = waist;
	}

	public String getSleeve() {
		return sleeve;
	}

	public void setSleeve(String sleeve) {
		this.sleeve = sleeve;
	}

	
}

package com.promelle.product.dto;

import java.util.List;
import java.util.Map;

import com.promelle.address.Address;
import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Product extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String title;
	private String description;
	private List<String> occasion;
	private List<String> category;
	private List<String> length;
	private List<String> type;
	private List<String> size;
	private String dressLength;
	private List<String> color;
	private List<String> careInstruction;
	private List<String> bustFit;
	private List<String> heightFit;
	private String brand;
	private String listAs;
	private Integer rentalPeriod;
	private Double rentalPrice;
	private Double originalPrice;
	private Map<String, String> images;
	private String ownerId;
	private String ownerName;
	private Double rating;
	private Integer reviewCount;
	private List<Map<String, Long>> rentedOn;
	private List<Long> notAvailableOn;
	private Boolean favorite;
	private Boolean availForOtherPeriod;
	private Address dressLocation;
	private User ownerDetails;
	private Double insuranceAmount;
	private List<String> waist;
	private List<String> sleeve;

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

	public List<String> getOccasion() {
		return occasion;
	}

	public void setOccasion(List<String> occasion) {
		this.occasion = occasion;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public List<String> getLength() {
		return length;
	}

	public void setLength(List<String> length) {
		this.length = length;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

	public List<String> getSize() {
		return size;
	}

	public void setSize(List<String> size) {
		this.size = size;
	}

	public String getDressLength() {
		return dressLength;
	}

	public void setDressLength(String dressLength) {
		this.dressLength = dressLength;
	}

	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color = color;
	}

	public List<String> getCareInstruction() {
		return careInstruction;
	}

	public void setCareInstruction(List<String> careInstruction) {
		this.careInstruction = careInstruction;
	}

	public List<String> getBustFit() {
		return bustFit;
	}

	public void setBustFit(List<String> bustFit) {
		this.bustFit = bustFit;
	}

	public List<String> getHeightFit() {
		return heightFit;
	}

	public void setHeightFit(List<String> heightFit) {
		this.heightFit = heightFit;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getListAs() {
		return listAs;
	}

	public void setListAs(String listAs) {
		this.listAs = listAs;
	}

	public Integer getRentalPeriod() {
		return rentalPeriod;
	}

	public void setRentalPeriod(Integer rentalPeriod) {
		this.rentalPeriod = rentalPeriod;
	}

	public Double getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(Double rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public List<Map<String, Long>> getRentedOn() {
		return rentedOn;
	}

	public void setRentedOn(List<Map<String, Long>> rentedOn) {
		this.rentedOn = rentedOn;
	}

	public List<Long> getNotAvailableOn() {
		return notAvailableOn;
	}

	public void setNotAvailableOn(List<Long> notAvailableOn) {
		this.notAvailableOn = notAvailableOn;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public Boolean getAvailForOtherPeriod() {
		return availForOtherPeriod;
	}

	public void setAvailForOtherPeriod(Boolean availForOtherPeriod) {
		this.availForOtherPeriod = availForOtherPeriod;
	}

	public Address getDressLocation() {
		return dressLocation;
	}

	public void setDressLocation(Address dressLocation) {
		this.dressLocation = dressLocation;
	}

	public User getOwnerDetails() {
		return ownerDetails;
	}

	public void setOwnerDetails(User ownerDetails) {
		this.ownerDetails = ownerDetails;
	}

	public Double getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(Double insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

	public List<String> getWaist() {
		return waist;
	}

	public void setWaist(List<String> waist) {
		this.waist = waist;
	}

	public List<String> getSleeve() {
		return sleeve;
	}

	public void setSleeve(List<String> sleeve) {
		this.sleeve = sleeve;
	}

}

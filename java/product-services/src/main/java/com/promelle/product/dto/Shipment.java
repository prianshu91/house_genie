package com.promelle.product.dto;

import java.util.List;
import java.util.Map;

import com.promelle.dto.AbstractAuditDTO;
import com.promelle.product.constants.ShipmentStatus;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Shipment extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String userId;
	private String userName;
	private String orderId;
	private String ownerId;
	private String ownerName;
	private ShipmentStatus shipmentStatus;
	private List<OrderItem> items;
	private Long createdOn;
	private Map<String, Long> track;
	private Earning earning;
	private User user;
	private User owner;
	private Product product;
	private Double deliveryCharge;
	private Double insuranceAmount;

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getShipmentStatus() {
		return shipmentStatus != null ? shipmentStatus.name() : null;
	}

	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = ShipmentStatus.valueOf(shipmentStatus
				.toUpperCase());
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Map<String, Long> getTrack() {
		return track;
	}

	public void setTrack(Map<String, Long> track) {
		this.track = track;
	}

	public Earning getEarning() {
		return earning;
	}

	public void setEarning(Earning earning) {
		this.earning = earning;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(Double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public Double getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(Double insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

}

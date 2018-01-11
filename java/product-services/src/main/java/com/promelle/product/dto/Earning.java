package com.promelle.product.dto;

import org.apache.commons.lang3.StringUtils;

import com.promelle.address.Address;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.product.constants.RedeemStatus;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Earning extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String userId;
	private String userName;
	private String shipmentId;
	private String orderId;
	private String ownerId;
	private String ownerName;
	private Double amount;
	private Double percentage;
	private RedeemStatus redeemStatus;
	private Long createdOn;
	private Long redeemOn;
	private Long redeemedOn;
	private String chequeNo;
	private String chequeImage;
	private String paymentMode;
	private String chequeRecipient;
	private ChequeAddress chequeAddress;
	private Double rentalPrice;
	private Double deliveryCharge;
	private Double redeemableAmount;
	private Long chequeDate;
	private String notes;
	private Long orderDate;

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

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getRedeemStatus() {
		return redeemStatus != null ? redeemStatus.name() : null;
	}

	public void setRedeemStatus(String redeemStatus) {
		this.redeemStatus = StringUtils.isNotBlank(redeemStatus) ? RedeemStatus
				.valueOf(redeemStatus.toUpperCase()) : null;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getRedeemOn() {
		return redeemOn;
	}

	public void setRedeemOn(Long redeemOn) {
		this.redeemOn = redeemOn;
	}

	public Long getRedeemedOn() {
		return redeemedOn;
	}

	public void setRedeemedOn(Long redeemedOn) {
		this.redeemedOn = redeemedOn;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getChequeImage() {
		return chequeImage;
	}

	public void setChequeImage(String chequeImage) {
		this.chequeImage = chequeImage;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getChequeRecipient() {
		return chequeRecipient;
	}

	public void setChequeRecipient(String chequeRecipient) {
		this.chequeRecipient = chequeRecipient;
	}

	public Address getChequeAddress() {
		return chequeAddress;
	}

	public void setChequeAddress(ChequeAddress chequeAddress) {
		this.chequeAddress = chequeAddress;
	}

	public Double getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(Double rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	public Double getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(Double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public Double getRedeemableAmount() {
		return redeemableAmount;
	}

	public void setRedeemableAmount(Double redeemableAmount) {
		this.redeemableAmount = redeemableAmount;
	}

	public Long getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Long chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}

}

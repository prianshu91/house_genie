package com.promelle.product.dto;

import java.util.List;
import java.util.Map;

import com.promelle.dto.AbstractAuditDTO;
import com.promelle.product.constants.OrderStatus;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Order extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String userId;
	private String userName;
	private OrderStatus orderStatus;
	private List<OrderItem> items;
	private List<Shipment> shipments;
	private OrderAddress shippingAddress;
	private Long createdOn;
	private Map<String, Long> track;
	private String transactionId;
	private String ccLast4;
	private String cardBranding;
	private Boolean inPersonDelivery;

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

	public String getOrderStatus() {
		return orderStatus != null ? orderStatus.name() : null;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = OrderStatus.valueOf(orderStatus.toUpperCase());
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public OrderAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(OrderAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
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

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCcLast4() {
		return ccLast4;
	}

	public void setCcLast4(String ccLast4) {
		this.ccLast4 = ccLast4;
	}

	public String getCardBranding() {
		return cardBranding;
	}

	public void setCardBranding(String cardBranding) {
		this.cardBranding = cardBranding;
	}

	public Boolean getInPersonDelivery() {
		return inPersonDelivery;
	}

	public void setInPersonDelivery(Boolean inPersonDelivery) {
		this.inPersonDelivery = inPersonDelivery;
	}
}

package com.promelle.product.dto;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding Order Message Conversation information.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class OrderMessageConversation extends AbstractDTO {
	private static final long serialVersionUID = 6963269359170891987L;
	private String senderId;
	private String receiverId;
	private String orderId;

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}

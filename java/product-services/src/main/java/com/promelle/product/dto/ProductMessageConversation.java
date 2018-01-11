package com.promelle.product.dto;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class ProductMessageConversation extends AbstractDTO {
	private static final long serialVersionUID = 6963269350970891987L;
	private String senderId;
	private String receiverId;
	private String productId;

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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}

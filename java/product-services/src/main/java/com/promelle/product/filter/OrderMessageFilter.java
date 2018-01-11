package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class OrderMessageFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269455127572138L;
	private String orderId;
	private String senderId;
	private String receiverId;
	private String conversationId;
	private Boolean isConversation;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public Boolean getIsConversation() {
		return isConversation;
	}

	public void setIsConversation(Boolean isConversation) {
		this.isConversation = isConversation;
	}

}

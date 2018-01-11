package com.promelle.product.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class ProductMessage extends AbstractAuditDTO {
	private static final long serialVersionUID = 6963269350970891987L;
	private String text;
	private String productId;
	private String productTitle;
	private String senderId;
	private String senderName;
	private String receiverId;
	private String receiverName;
	private Long timestamp;
	private String conversationId;
	private boolean read;
	private Integer unreadCount;
	private boolean isNew;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Integer getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(Integer unreadCount) {
		this.unreadCount = unreadCount;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}

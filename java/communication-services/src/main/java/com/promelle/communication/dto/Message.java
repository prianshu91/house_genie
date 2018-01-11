package com.promelle.communication.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class Message extends AbstractAuditDTO {
	private static final long serialVersionUID = 6963269350970891987L;
	private String text;
	private String senderId;
	private String senderName;
	private String receiverId;
	private String receiverName;
	private Long timestamp;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

}

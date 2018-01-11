package com.promelle.communication.dto;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding broadcast email information.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class BroadcastEmail extends AbstractDTO {
	private static final long serialVersionUID = 6963269350970901987L;

	private String to;
	private String from;
	private String subject;
	private String body;
	private String userName;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

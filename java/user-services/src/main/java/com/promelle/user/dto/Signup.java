package com.promelle.user.dto;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.databind.JsonNode;
import com.promelle.dto.AbstractDTO;
import com.promelle.utils.UUIDUtils;

/**
 * This class is intended for holding signup information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class Signup extends AbstractDTO {
	private static final long serialVersionUID = -7014364639549246150L;

	@Email
	private String email;
	private String name;
	private String accountName;
	private String portalName;
	private String mobile;
	private Integer status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email != null ? email.toLowerCase() : null;
	}

	public void setEmail(String email) {
		this.email = email != null ? email.toLowerCase() : null;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static Signup parse(JsonNode node) {
		Signup signup = new Signup();
		signup.setId(node.has("id") ? node.findValue("id").asText() : UUIDUtils
				.getUUID());
		signup.setEmail(node.findValue("email").asText());
		if (node.findValue("name") != null) {
			signup.setName(node.findValue("name").asText());
		}
		signup.setAccountName(node.findValue("accountName").asText());
		signup.setMobile(node.findValue("mobile") != null ? node.findValue(
				"mobile").asText() : null);
		signup.setStatus(2);
		signup.setPortalName(node.findValue("accountName").asText()
				.replaceAll("[^a-zA-Z]", "").toLowerCase());
		return signup;
	}

}

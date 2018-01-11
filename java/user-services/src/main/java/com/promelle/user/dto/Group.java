package com.promelle.user.dto;

import com.promelle.dto.AbstractAuditDTO;


/**
 * This class is intended for holding group information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Group extends AbstractAuditDTO {
	private static final long serialVersionUID = 5223725113726741469L;
	private String name;
    private String code;
    private String description;
    private String accountId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

}

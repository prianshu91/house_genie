package com.promelle.user.dto;

import com.promelle.dto.AbstractAuditDTO;


/**
 * This class is intended for holding role information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Role extends AbstractAuditDTO {
	private static final long serialVersionUID = -7180772785016498495L;
	private String name;
    private String code;
    private String description;

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

}

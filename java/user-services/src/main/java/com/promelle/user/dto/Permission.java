package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding permission information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Permission extends AbstractDTO {
	private static final long serialVersionUID = 8640056693709044035L;
	private String name;
    private String code;
    private String description;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

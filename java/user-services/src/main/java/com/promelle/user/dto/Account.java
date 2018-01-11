package com.promelle.user.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding account information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class Account extends AbstractAuditDTO {
	private static final long serialVersionUID = 8526710887033850810L;
	private String name;
    private String portalName;
    private String ownerId;
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}

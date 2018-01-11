package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding excluded permissions information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class PermissionExclusion extends AbstractDTO {
	private static final long serialVersionUID = -3536613544287093285L;
	private String type;
    private String permissionId;
    private String refId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

}

package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding user permission association information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserPermission extends AbstractDTO {
	private static final long serialVersionUID = -9181203232803846521L;
	private String userId;
    private String permissionId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

}

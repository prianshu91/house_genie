package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding role permission association information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class RolePermission extends AbstractDTO {
	private static final long serialVersionUID = -2189753818075492903L;
	private String roleId;
    private String permissionId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

}

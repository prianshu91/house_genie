package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding user role association information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserRole extends AbstractDTO {
	private static final long serialVersionUID = 5587506022135984474L;
	private String userId;
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}

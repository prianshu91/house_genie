package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding group role association information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class GroupRole extends AbstractDTO {
	private static final long serialVersionUID = -3923332069364366020L;
	private String roleId;
    private String groupId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}

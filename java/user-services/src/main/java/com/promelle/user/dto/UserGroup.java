package com.promelle.user.dto;

import com.promelle.dto.AbstractDTO;


/**
 * This class is intended for holding user group association information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserGroup extends AbstractDTO {
	private static final long serialVersionUID = -8540555217542522825L;
	private String userId;
    private String groupId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}

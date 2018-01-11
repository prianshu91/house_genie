package com.promelle.ui.dto;

import java.util.List;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding group information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class LoginSession extends AbstractDTO {
	private static final long serialVersionUID = 7285702577347527285L;
	private String userId;
    private Long createdOn;
    private Long modifiedOn;
    private String response;
    private String timeZone;
    private List<String> permissions;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}

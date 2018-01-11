package com.promelle.dto;


/**
 * This class is responsible for holding generic fields to be returned in response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AbstractAuditDTO extends AbstractDTO {
	private static final long serialVersionUID = -6507861095655756805L;
	public static final String STATUS = "status";
    public static final String CREATED_ON = "createdOn";
    public static final String MODIFIED_ON = "modifiedOn";
    public static final String CREATED_BY = "createdBy";
    public static final String MODIFIED_BY = "modifiedBy";

    private String createdBy;
    private String modifiedBy;
    private Long createdOn;
    private Long modifiedOn;
    private Integer status;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

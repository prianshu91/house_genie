package com.promelle.user.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserFilter extends SearchFilter {
	private static final long serialVersionUID = 7768269451527572138L;
	private String accountId;
	private String name;
	private String email;
	private String mobile;
	private String username;
	private String portalName;
	private String roleId;
	private String groupId;
	private String permissionId;
	private Integer status;
	private String otpPurpose;
	private String searchText;
	private Long searchDate;
	private String text;

	// for getting School Pod members list
	private String schoolId;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email != null ? email.toLowerCase() : null;
	}

	public void setEmail(String email) {
		this.email = email != null ? email.toLowerCase() : null;
	}

	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}

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

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOtpPurpose() {
		return otpPurpose;
	}

	public void setOtpPurpose(String otpPurpose) {
		this.otpPurpose = otpPurpose;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Long getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Long searchDate) {
		this.searchDate = searchDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

}

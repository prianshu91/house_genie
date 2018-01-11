package com.promelle.user.filter;

import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class SchoolFilter extends SearchFilter {
	private static final long serialVersionUID = 7768723451527125650L;
	private String schoolName;
	private Integer status;
	private String schoolPodName;

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSchoolPodName() {
		return schoolPodName;
	}

	public void setSchoolPodName(String schoolPodName) {
		this.schoolPodName = schoolPodName;
	}

}

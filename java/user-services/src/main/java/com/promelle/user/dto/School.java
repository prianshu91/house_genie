package com.promelle.user.dto;

import java.util.List;

import com.promelle.address.Address;
import com.promelle.dto.AbstractAuditDTO;

/**
 * This class is intended for holding school information.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class School extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895750L;
	private String schoolName;
	private Address schoolAddress;

	// School Pod implementation fields
	private String schoolPodName;
	private String schoolPodLogo;
	private List<User> podMembers;

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Address getSchoolAddress() {
		return schoolAddress;
	}

	public void setSchoolAddress(Address schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public String getSchoolPodName() {
		return schoolPodName;
	}

	public void setSchoolPodName(String schoolPodName) {
		this.schoolPodName = schoolPodName;
	}

	public String getSchoolPodLogo() {
		return schoolPodLogo;
	}

	public void setSchoolPodLogo(String schoolPodLogo) {
		this.schoolPodLogo = schoolPodLogo;
	}

	public List<User> getPodMembers() {
		return podMembers;
	}

	public void setPodMembers(List<User> podMembers) {
		this.podMembers = podMembers;
	}

}

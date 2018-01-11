package com.promelle.product.dto;

import com.promelle.address.Address;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ChequeAddress extends Address {
	private static final long serialVersionUID = -6893401802017898852L;
	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}

package com.promelle.communication.dto;

import com.promelle.address.Address;
import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding user information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Product extends AbstractDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String title;
	private String ownerId;
	private String ownerName;
	private Address dressLocation;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Address getDressLocation() {
		return dressLocation;
	}

	public void setDressLocation(Address dressLocation) {
		this.dressLocation = dressLocation;
	}

}

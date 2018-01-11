package com.promelle.product.dto;

import java.util.Comparator;

import com.promelle.dto.AbstractAuditDTO;

/**
 * This class holds Dress Rental information
 * 
 * @author Kanak Sony
 *
 */
public class DressRentalItem extends AbstractAuditDTO implements
		Comparator<DressRentalItem>, Comparable<DressRentalItem> {
	private static final long serialVersionUID = 3458154745040710421L;
	private OrderItem OrderItem;
	private User lender;
	private User renter;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public OrderItem getOrderItem() {
		return OrderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		OrderItem = orderItem;
	}

	@Override
	public int compareTo(DressRentalItem dressRentalItem) {
		return 0;
	}

	@Override
	public int compare(DressRentalItem dressRentalItem1,
			DressRentalItem dressRentalItem2) {
		return Long.compare(dressRentalItem1.getOrderItem().getCreatedOn(),
				dressRentalItem2.getOrderItem().getCreatedOn());
	}

	public void setLender(User user) {
		this.lender = user;
	}

	public void setRenter(User user) {
		this.renter = user;
	}
	
	public User getLender() {
		return this.lender;
	}

	public User getRenter() {
		return this.renter;
	}
}

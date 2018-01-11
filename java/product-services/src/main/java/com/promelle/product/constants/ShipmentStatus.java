package com.promelle.product.constants;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public enum ShipmentStatus {

	SHIPMENT_PENDING(1), SHIPMENT_DELIVERED(2), SHIPMENT_CANCELLED(0), SHIPMENT_CANCELLED_BY_LENDER(
			0), SHIPMENT_CANCELLED_BY_RENTER(0), SHIPMENT_COMPLETED(3);

	private int sequence;

	private ShipmentStatus(int sequence) {
		this.sequence = sequence;
	}

	public int getSequence() {
		return sequence;
	}

	public static boolean isValidStatus(String oldStatus, String newStatus) {
		int oldSequece = valueOf(oldStatus).getSequence();
		int newSequece = valueOf(newStatus).getSequence();
		if (newSequece == 0) {
			return true;
		}
		return (oldSequece + 1) == newSequece;
	}

}

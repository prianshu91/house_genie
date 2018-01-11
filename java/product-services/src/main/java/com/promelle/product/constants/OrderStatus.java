package com.promelle.product.constants;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public enum OrderStatus {

	ORDER_PENDING(1), ORDER_CANCELLED(0), TRANSACTION_SUCCESSFUL(1), TRANSACTION_FAILED(
			1), ORDER_PLACED(2);

	private int sequence;

	private OrderStatus(int sequence) {
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

package com.promelle.payment.utils;

/**
 * This class is responsible for providing payment related utility functions.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public final class PaymentUtils {

	public PaymentUtils() {
		// default constructor stub
	}

	public static Double getInsuranceAmountFromPrice(Double currentPrice) {

		if (currentPrice <= 24.99) {
			return 2.99;
		} else if (currentPrice > 24.99 && currentPrice <= 49.99) {
			return 4.99;
		} else if (currentPrice > 49.99) {
			return 6.99;
		} else {
			return 0.00;
		}
	}
}

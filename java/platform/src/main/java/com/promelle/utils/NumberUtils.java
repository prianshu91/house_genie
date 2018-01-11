package com.promelle.utils;

import java.text.DecimalFormat;

/**
 * This class is intended for providing number functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class NumberUtils {

	/**
	 * This function is responsible for wrapping up double value up to 2 decimal
	 * places.
	 * 
	 * @param val
	 * @return precise value
	 */
	public static double getPrecisedDouble(Double val) {
		return getPrecisedDouble(val, 2);
	}

	/**
	 * This function is responsible for wrapping up double value up to 2 decimal
	 * places.
	 * 
	 * @param val
	 * @return precise value
	 */
	public static double getPrecisedDouble(Double val, int decimalPlaces) {
		return Double.parseDouble(new DecimalFormat("#0."
				+ TextUtils.getRepeatingCharString('0', decimalPlaces))
				.format(val));
	}

	public static int randIntBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	public static long randLongBetween(long start, long end) {
		return start + (long) Math.round(Math.random() * (end - start));
	}

}

package com.promelle.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * This class is intended for providing date functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class OTPUtils {

	public static String getOTP(int size) {
		try {
			StringBuilder otp = new StringBuilder();
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0; i < size; i++) {
				otp.append(number.nextInt(9));
			}
			return otp.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}

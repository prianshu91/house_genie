package com.promelle.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.exception.SecurityException;

/**
 * This class is intended for providing hash functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class HashUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HashUtils.class.getName());

	private static final int BITMASK = 0X000000FF;
	private static final int SIXTEEN = 16;

	/**
	 * This function is responsible for providing MD5 hash
	 * 
	 * @param input
	 * @return hash
	 * @throws SecurityException
	 * @throws Security
	 */
	public static String getMD5Hash(String input) throws SecurityException {
		byte[] data = input.getBytes();
		String hex = "";
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e);
		}
		md.update(data, 0, data.length);
		byte[] b = md.digest();
		int msb;
		int lsb = 0;
		for (int i = 0; i < b.length; i++) {
			msb = ((int) b[i] & BITMASK) / SIXTEEN;
			lsb = ((int) b[i] & BITMASK) % SIXTEEN;
			hex = hex + hexChars[msb] + hexChars[lsb];
		}
		LOGGER.trace(String.format("getMD5Hash(%s) = %s", input, hex));
		return hex;
	}

	/**
     * This function Hash a string
     * 
     * @param value
     *            to be encrypted
     * @return String Encrypted string
     */
    public static String encrypt(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt());
    }

    /**
     * Check that an unencrypted password matches one that has previously been hashed
     * 
     * @param plaintext
     *            to be matched
     * @param hashed
     *            pre-hashed value
     * @return Boolean true if hash is matched
     */

    public static boolean validate(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

}

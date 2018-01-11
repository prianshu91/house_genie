package com.promelle.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is intended for providing application wide common functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class UUIDUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UUIDUtils.class.getName());

	private static final int HALF_BITMASK = 0xFF;

	/**
	 * This function is responsible for converting UUID string (hex format) into
	 * binary format.
	 * 
	 * @param str
	 *            UUID string in hex format
	 * @return binary representation of hex string
	 */
	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		}
		char[] hex = str.toCharArray();
		int length = hex.length / 2;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++) {
			int value = (Character.digit(hex[i * 2], 16) << 4)
					| Character.digit(hex[i * 2 + 1], 16);
			if (value > 127) {
				value -= 256;
			}
			raw[i] = (byte) value;
		}
		return raw;
	}

	/**
	 * This function is responsible for converting binary representation of UUID
	 * to hex format.
	 * 
	 * @param bytes
	 *            binary representation of the UUID
	 * @param max
	 *            maximum number bytes to be used from byte[] for converstion
	 * @return hex representation of UUID
	 */
	public static String bytesToHex(byte[] bytes, long max) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length && i < max; i++) {
			String hex = Integer.toHexString(bytes[i] & HALF_BITMASK);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			buffer.append(hex);
		}
		return buffer.toString().toUpperCase();
	}

	/**
	 * <p>
	 * Helper method to get binary version (byte[16]) of the string format of
	 * UUID.
	 * </p>
	 * 
	 * @param uuid
	 *            string representation of the UUID to be converted
	 * @return binary representation of the given UUID
	 */
	public static byte[] getBinaryUUID(String uuid) {
		UUID u = UUID.fromString(uuid);
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.putLong(u.getMostSignificantBits()).putLong(
				u.getLeastSignificantBits());
		return bb.array();
	}

	/**
	 * <p>
	 * Helper method to check given string for valid UUID.
	 * </p>
	 * 
	 * @param uuid
	 *            string representation of the UUID to be converted
	 * @return true if given UUID string is valid, false otherwise
	 */
	public static boolean isUUID(String uuid) {
		UUID u = null;
		try {
			u = UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			LOGGER.trace("Illegal Argument Exception", e);
		}
		return u == null ? false : true;
	}

	/**
	 * <p>
	 * Helper method to validate UUID string.
	 * </p>
	 * 
	 * @param uuid
	 *            string representation of the UUID to be converted
	 * @param fieldName
	 *            name of the field for against which the validation is to be
	 *            performed
	 * @return true if given UUID string is valid, false otherwise
	 * @throws IllegalArgumentException
	 *             if given UUID is can not be parsed as valid UUID
	 */
	public static boolean validateUUID(String uuid, String fieldName) {
		if (!isUUID(uuid)) {
			throw new IllegalArgumentException(
					fieldName
							+ " should be a valid string representation of UUID or GUID");
		}
		return true;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}

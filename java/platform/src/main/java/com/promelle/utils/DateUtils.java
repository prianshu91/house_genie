package com.promelle.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is intended for providing date functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class DateUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DateUtils.class.getName());
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd";

	public static final String[] SUPPORTED_FORMATS = { "yyyy/MM/dd",
			"MM/dd/yyyy hh:mm ##", "yyyy-MM-dd", "yyyyMMdd", "yyMMdd",
			"EEE, dd MMM yyyy HH:mm:ss ZZZ", "yyyy-MM-dd HH:mm:ssZ",
			"yyyy-MM-dd HH:mm:ssZZZZZ", "yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd HH:mm:ss.SSS", "MM/dd/yyyy HH:mm",
			"EEE, dd MMM yyyy HH:mm:ss ZZZZZ", "yyyy-MM-ddTHH:mm:ss.SSSZZZZZZ",
			"yyyy-MM-ddTHH:mm:ss.SSSZZZZZ", "yyyy-MM-ddTHH:mm:ss",
			"yyyy-MM-ddTHH:mm:ss.SSSZ", "MMM dd, yyyy", "MMM d, yyyy",
			"MMM dd, yyyy hh:mm a# ZZZ", "MMM dd, yyyy hh:mm a# ZZZZZ" };

	/**
	 * This function is responsible for parsing a date string to java.util.Date
	 * 
	 * Supported Formats : "yyyy/MM/dd", "yyyy-MM-dd",
	 * "yyyyMMdd","yyMMdd","yyMMdd", "EEE, dd MMM yyyy HH:mm:ss ZZZ",
	 * "yyyy-MM-ddTHH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-ddTHH:mm:ss.SSS"
	 * 
	 * @param dateString
	 * @return java.util.Date
	 */
	public static Date parseDate(String str) {
		String dateString = str;
		for (String format : SUPPORTED_FORMATS) {
			String regMatch = format.replaceAll("y|d|M|H|m|s|S|Z|E|h|a|#", "_");
			if (TextUtils.like(dateString, regMatch)) {
				format = format.replace("#", "");
				if (format.contains("ZZZZZZ")) {
					int pos = format.indexOf("ZZZZZZ") + 3;
					dateString = dateString.substring(0, pos)
							+ dateString
									.substring(pos + 1, dateString.length());
				}
				format = format.replaceAll("[Z]+", "Z");
				if (format.contains("T")) {
					format = format.replace("T", "'T'");
				}
				if (dateString.contains("Z")) {
					format = format.replaceAll("Z", "");
					dateString = dateString.replaceAll("Z", "");
				}
				try {
					return new SimpleDateFormat(format).parse(dateString);
				} catch (Exception e) {
					LOGGER.error("Error in parseDate()", e);
				}
			}
		}
		return null;
	}

	public static long getCurrentTimestamp() {
		return Calendar.getInstance().getTimeInMillis();
	}

	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public static String getCurrentDateString() {
		return new SimpleDateFormat(DEFAULT_FORMAT).format(Calendar
				.getInstance().getTime());
	}

	public static String getCurrentDateString(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance()
				.getTime());
	}

	public static String getDateString(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return new SimpleDateFormat(DEFAULT_FORMAT).format(calendar.getTime());
	}

	public static String getDateString(long timestamp, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	public static long getMonthStartTimestamp() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

}

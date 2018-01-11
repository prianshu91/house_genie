package com.promelle.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is intended for providing exception functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class ExceptionUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExceptionUtils.class.getName());

	/**
	 * This function is responsible for converting stack trace to string.
	 * 
	 * @param e
	 * @return stack trace string.
	 */
	public static String stackTraceToString(Throwable e) {
		String res = null;
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			res = stringWriter.toString();
			printWriter.close();
			stringWriter.close();
		} catch (IOException ignore) {
			LOGGER.trace("Ignore", e);
		}
		return res;
	}

}

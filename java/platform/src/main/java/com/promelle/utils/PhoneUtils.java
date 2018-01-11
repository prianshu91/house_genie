package com.promelle.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This class is intended for providing phone no functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class PhoneUtils {

	public static boolean compare(String one, String two) {
		if (StringUtils.isBlank(one) || StringUtils.isBlank(two)) {
			return false;
		}
		return one.replaceAll("[^\\d.]", "").trim()
				.equalsIgnoreCase(two.replaceAll("[^\\d.]", "").trim());
	}

	public static boolean compare(List<String> one, List<String> two) {
		if (CollectionUtils.isEmpty(one) || CollectionUtils.isEmpty(two)) {
			return false;
		}
		boolean flag = false;
		for (String o : one) {
			for (String t : two) {
				flag = flag || compare(o, t);
				if (flag) {
					return true;
				}
			}
		}
		return flag;
	}

}

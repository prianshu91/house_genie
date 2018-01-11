package com.promelle.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This class is intended for providing application wide common functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class WeblinkUtils {

	public static boolean compare(String first, String second) {
		String one = first;
		String two = second;
		if (StringUtils.isBlank(one) || StringUtils.isBlank(two)) {
			return false;
		}
		while (one.endsWith("/")) {
			one = one.replaceAll("/$", "");
		}
		if (!one.startsWith("http")) {
			one = "http://" + one;
		}
		one = one.replaceAll("http(s)?://(www.)?", "").trim();
		if (!two.startsWith("http")) {
			two = "http://" + two;
		}
		while (two.endsWith("/")) {
			two = two.replaceAll("/$", "");
		}
		two = two.replaceAll("http(s)?://(www.)?", "").trim();
		return one.equalsIgnoreCase(two);
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

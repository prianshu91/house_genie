package com.promelle.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This class is intended for providing list functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class ListUtils {

	/**
	 * This function is responsible for converting string into trimmed list
	 * 
	 * @param originalStr
	 * @param split
	 * @return
	 */
	public static List<String> splitStringIntoTrimmedList(String originalStr,
			String split) {
		List<String> list = new LinkedList<String>();
		if (StringUtils.isBlank(originalStr)) {
			return list;
		}
		for (String str : originalStr.split(split)) {
			str = str.trim();
			if (str.length() > 0) {
				list.add(str);
			}
		}
		return list;
	}

	/**
	 * This function is intended for getting the most popular element from list
	 * 
	 * @param list
	 * @return Most Popular Element
	 */
	public static <T> T getMostOccouringElement(List<T> list) {
		int size = list.size();
		if (size == 0) {
			return null;
		}
		int count = 0;
		int maxCount = 0;
		T element = list.get(0);
		T mostOccuringElement = element;
		for (int index = 0; index < size; index++) {
			if (list.get(index).equals(element)) {
				count++;
				if (count > maxCount) {
					maxCount = count;
					mostOccuringElement = element;
				}
			} else {
				count = 1;
			}
			element = list.get(index);
		}
		return mostOccuringElement;
	}

}

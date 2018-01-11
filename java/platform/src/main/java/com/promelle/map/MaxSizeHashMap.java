package com.promelle.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is intended for MaxSizeHashMap
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -2669830632767431933L;
	private final int maxSize;

	public MaxSizeHashMap(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}

}

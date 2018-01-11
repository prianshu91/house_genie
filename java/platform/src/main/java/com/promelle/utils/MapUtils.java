package com.promelle.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * This class is intended for providing map functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class MapUtils {

	/**
	 * This function is responsible for retrieving a string value from a map
	 * 
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return defaultValue if string is empty
	 */
	public static String getStringValue(Map<String, Object> map, String key,
			String defaultValue) {
		if (map != null && map.containsKey(key) && map.get(key) != null) {
			String value = String.valueOf(map.get(key));
			return StringUtils.isEmpty(value) ? defaultValue : value;
		}
		return defaultValue;
	}

	public static Boolean getBooleanValue(Map<String, Object> map, String key,
			Boolean defaultValue) {
		if (map != null && map.containsKey(key) && map.get(key) != null) {
			String value = String.valueOf(map.get(key));
			return value == null ? defaultValue : Boolean.valueOf(value);
		}
		return defaultValue;
	}

	public static Integer getIntegerValue(Map<String, Object> map, String key,
			Integer defaultValue) {
		if (map != null && map.containsKey(key) && map.get(key) != null) {
			String value = String.valueOf(map.get(key));
			return value == null ? defaultValue : Integer.valueOf(value);
		}
		return defaultValue;
	}

	public static Long getLongValue(Map<String, Object> map, String key,
			Long defaultValue) {
		if (map != null && map.containsKey(key) && map.get(key) != null) {
			String value = String.valueOf(map.get(key));
			return value == null ? defaultValue : Long.valueOf(value);
		}
		return defaultValue;
	}

	public static Double getDoubleValue(Map<String, Object> map, String key,
			Double defaultValue) {
		if (map != null && map.containsKey(key) && map.get(key) != null) {
			String value = String.valueOf(map.get(key));
			return value == null ? defaultValue : Double.valueOf(value);
		}
		return defaultValue;
	}

	/**
	 * Calculates the frequency map of all words in the given text.
	 * 
	 * @param text
	 *            text for which frequency map is to be prepared.
	 * @param deilimter
	 *            delimiter to be used for tokenization
	 * @return the frequency map of all words from the given text
	 */
	public static Map<String, Integer> getFrequencyMap(String text,
			String delimiter) {
		Map<String, Integer> freqMap = new HashMap<String, Integer>();
		StringTokenizer tokenizer = new StringTokenizer(text, delimiter);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			freqMap.put(token,
					freqMap.containsKey(token) ? freqMap.get(token) + 1 : 1);
		}
		return freqMap;
	}

	/**
	 * This function is responsible for sorting of map against inner map field.
	 * 
	 * @param map
	 * @return sorted map
	 */
	public static Map<String, Map<String, Object>> sortByInnerField(
			Map<String, Map<String, Object>> map, final String field) {
		List<Map.Entry<String, Map<String, Object>>> list = new LinkedList<Map.Entry<String, Map<String, Object>>>(
				map.entrySet());

		Collections.sort(list,
				new Comparator<Map.Entry<String, Map<String, Object>>>() {
					public int compare(
							Map.Entry<String, Map<String, Object>> m1,
							Map.Entry<String, Map<String, Object>> m2) {
						if (m2.getValue().get(field) instanceof Double) {
							return ((Double) m2.getValue().get(field))
									.compareTo((Double) m1.getValue()
											.get(field));
						} else if (m2.getValue().get(field) instanceof Integer) {
							return ((Integer) m2.getValue().get(field))
									.compareTo((Integer) m1.getValue().get(
											field));
						} else if (m2.getValue().get(field) instanceof Float) {
							return ((Float) m2.getValue().get(field))
									.compareTo((Float) m1.getValue().get(field));
						} else if (m2.getValue().get(field) instanceof Long) {
							return ((Long) m2.getValue().get(field))
									.compareTo((Long) m1.getValue().get(field));
						} else {
							return (String.valueOf(m2.getValue().get(field)))
									.compareTo(String.valueOf(m1.getValue()
											.get(field)));
						}
					}
				});

		Map<String, Map<String, Object>> result = new LinkedHashMap<String, Map<String, Object>>();
		for (Map.Entry<String, Map<String, Object>> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static class MapGenericUtils<K, V extends Comparable<V>> {

		/**
		 * This function is intended for sorting a map against value field
		 * 
		 * @param map
		 * @return Sorted map
		 */
		public Map<K, V> sortByValue(Map<K, V> map) {
			List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
				public int compare(Map.Entry<K, V> m1, Map.Entry<K, V> m2) {
					return (m2.getValue()).compareTo(m1.getValue());
				}
			});
			Map<K, V> result = new LinkedHashMap<K, V>();
			for (Map.Entry<K, V> entry : list) {
				result.put(entry.getKey(), entry.getValue());
			}
			return result;
		}

		/**
		 * This function is intended for sorting a map against value field
		 * 
		 * @param map
		 * @return Sorted map
		 */
		public List<K> sortByValueAndReturnKeySetAsList(Map<K, V> map) {
			List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
				public int compare(Map.Entry<K, V> m1, Map.Entry<K, V> m2) {
					return (m2.getValue()).compareTo(m1.getValue());
				}
			});
			List<K> result = new LinkedList<K>();
			for (Map.Entry<K, V> entry : list) {
				result.add(entry.getKey());
			}
			return result;
		}

		/**
		 * This function is intended for sorting a map against value field and
		 * there by return first n elements
		 * 
		 * @param map
		 * @return Sorted map
		 */
		public Map<K, V> sortByValue(Map<K, V> map, int n) {
			List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
				public int compare(Map.Entry<K, V> m1, Map.Entry<K, V> m2) {
					return (m2.getValue()).compareTo(m1.getValue());
				}
			});
			list = list.size() >= n ? list.subList(0, n) : list;
			Map<K, V> result = new LinkedHashMap<K, V>();
			for (Map.Entry<K, V> entry : list) {
				result.put(entry.getKey(), entry.getValue());
			}
			return result;
		}

		/**
		 * This function is intended for sorting a map against value field and
		 * there by return first n elements
		 * 
		 * @param map
		 * @return Sorted map
		 */
		public List<K> sortByValueAndReturnKeySetAsList(Map<K, V> map, int n) {
			List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
				public int compare(Map.Entry<K, V> m1, Map.Entry<K, V> m2) {
					return (m2.getValue()).compareTo(m1.getValue());
				}
			});
			list = list.size() >= n ? list.subList(0, n) : list;
			List<K> result = new LinkedList<K>();
			for (Map.Entry<K, V> entry : list) {
				result.add(entry.getKey());
			}
			return result;
		}

	}

	/**
	 * This function is responsible for bifurcating map key to multilevel inner
	 * map.
	 * 
	 * @param map
	 * @param delimiter
	 * @return result Map
	 */
	@SuppressWarnings("all")
	public static Map splitKeyToInnerMap(Map map, String delim) {
		Map resMap = new HashMap();
		String delimiter = StringUtils.isNotBlank(delim) ? delim : "_";
		for (Object key : map.keySet()) {
			String[] keys = ((String) key).split(delimiter);
			if (keys.length == 1) {
				return map;
			}
			String newKey = "";
			for (int i = 0; i <= keys.length - 2; i++) {
				newKey += keys[i] + delimiter;
			}
			newKey = newKey.substring(0, newKey.length() - 1);
			Map<String, Object> newMap = null;
			if (resMap.containsKey(newKey)) {
				newMap = (Map<String, Object>) resMap.get(newKey);
			} else {
				newMap = new HashMap<String, Object>();
			}
			newMap.put(keys[keys.length - 1], map.get((String) key));
			resMap.put(newKey, newMap);
		}
		return splitKeyToInnerMap(resMap, delimiter);
	}

	public static Map<String, Double> getCombinedFrequencyMap(
			Map<String, Double> map, Map<String, Double> secondMap) {
		double newValue = 0;
		for (String key : map.keySet()) {
			if (!secondMap.containsKey(key)) {
				newValue = map.get(key);
				secondMap.put(key, newValue);
				continue;
			}
			double currentValue = secondMap.get(key);
			newValue = map.get(key);
			currentValue = currentValue + newValue;
			secondMap.put(key, currentValue);
		}
		return secondMap;
	}

	public static void getCombinedFrequencyMap(Map<String, Double> firstMap,
			Map<String, Double> secondMap, Map<String, Double> thirdMap) {
		for (String key : firstMap.keySet()) {
			if (secondMap != null) {
				if (secondMap.containsKey(key)) {
					secondMap.put(key, secondMap.get(key) + firstMap.get(key));
				} else {
					secondMap.put(key, firstMap.get(key));
				}
			}
			if (thirdMap != null) {
				if (thirdMap.containsKey(key)) {
					thirdMap.put(key, 1d + thirdMap.get(key));
				} else {
					thirdMap.put(key, 1d);
				}
			}
		}
	}

	public static Map<String, Integer> getCombinedFrequencyMapForInteger(
			Map<String, Integer> map, Map<String, Integer> secondMap) {
		Integer newValue = 0;
		for (String key : map.keySet()) {
			if (!secondMap.containsKey(key)) {
				newValue = map.get(key);
				secondMap.put(key, newValue);
				continue;
			}
			Integer currentValue = secondMap.get(key);
			newValue = map.get(key);
			currentValue = currentValue + newValue;
			secondMap.put(key, currentValue);
		}
		return secondMap;
	}

	/**
	 * This function is responsible for sorting of map against inner map field.
	 * 
	 * @param map
	 * @return sorted map
	 */
	public static Map<String, Map<String, Double>> sortByInnerMapField(
			Map<String, Map<String, Double>> map, final String field) {
		List<Map.Entry<String, Map<String, Double>>> list = new LinkedList<Map.Entry<String, Map<String, Double>>>(
				map.entrySet());

		Collections.sort(list,
				new Comparator<Map.Entry<String, Map<String, Double>>>() {
					public int compare(
							Map.Entry<String, Map<String, Double>> m1,
							Map.Entry<String, Map<String, Double>> m2) {
						return ((Double) m2.getValue().get(field))
								.compareTo((Double) m1.getValue().get(field));
					}
				});

		Map<String, Map<String, Double>> result = new LinkedHashMap<String, Map<String, Double>>();
		for (Map.Entry<String, Map<String, Double>> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * This function is responsible for sorting of map against inner map field.
	 * 
	 * @param map
	 * @return sorted map
	 */
	public static Map<String, Map<String, Double>> sortByInnerMapField(
			Map<String, Map<String, Double>> map, final String field, int n) {
		List<Map.Entry<String, Map<String, Double>>> list = new LinkedList<Map.Entry<String, Map<String, Double>>>(
				map.entrySet());

		Collections.sort(list,
				new Comparator<Map.Entry<String, Map<String, Double>>>() {
					public int compare(
							Map.Entry<String, Map<String, Double>> m1,
							Map.Entry<String, Map<String, Double>> m2) {
						return ((Double) m2.getValue().get(field))
								.compareTo((Double) m1.getValue().get(field));
					}
				});

		Map<String, Map<String, Double>> result = new LinkedHashMap<String, Map<String, Double>>();
		list = list.size() >= n ? list.subList(0, n) : list;
		for (Map.Entry<String, Map<String, Double>> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void put(Map<String, Object> obj, String field, Object val) {
        if (val != null) {
            obj.put(field, val);
        }
    }

}

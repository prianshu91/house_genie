package com.promelle.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * This class is intended for providing json functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class JsonUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JsonUtils.class.getName());

	/**
	 * This function is responsible for converting map to json string
	 * 
	 * @param map
	 * @return json string
	 */
	public static String getJsonString(Object obj) {
		String res = "{}";
		StringWriter out = new StringWriter();
		try {
			JSONValue.writeJSONString(obj, out);
			res = out.toString();
		} catch (IOException e) {
			LOGGER.error("Error converting obj to string", e);
		}
		return res;
	}

	/**
	 * This function is responsible for retrieving a child json node as a string
	 * from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return string value
	 */
	public static String getStringValue(JsonNode node, String fieldName,
			String defaultValue) {
		if (node != null) {
			JsonNode childNode = node.get(fieldName);
			if (childNode != null) {
				String text = childNode.asText();
				if (StringUtils.isNotEmpty(text)
						&& !text.equalsIgnoreCase("null")) {
					return text;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * This function is responsible for retrieving a child json node as a
	 * {@code Long} from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return {@code Long} value
	 */
	public static Long getLongValue(JsonNode node, String fieldName,
			Long defaultValue) {
		if (node != null) {
			JsonNode childNode = node.get(fieldName);
			if (childNode != null) {
				Long val = childNode.asLong();
				if (val != null) {
					return val;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * This function is responsible for retrieving a child json node as a
	 * {@code Integer} from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return {@code Integer} value
	 */
	public static Integer getIntValue(JsonNode node, String fieldName,
			Integer defaultValue) {
		if (node != null) {
			JsonNode childNode = node.get(fieldName);
			if (childNode != null) {
				Integer val = childNode.asInt();
				if (val != null) {
					return val;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * This function is responsible for retrieving a child json node as a
	 * {@code Boolean} from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return {@code Boolean} value
	 */
	public static Boolean getBooleanValue(JsonNode node, String fieldName,
			Boolean defaultValue) {
		if (node != null) {
			JsonNode childNode = node.get(fieldName);
			if (childNode != null) {
				Boolean val = childNode.asBoolean();
				if (val != null) {
					return val;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * This function is responsible for retrieving a child json node as a
	 * {@code Double} from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return {@code Double} value
	 */
	public static Double getDoubleValue(JsonNode node, String fieldName,
			Double defaultValue) {
		if (node != null) {
			JsonNode childNode = node.get(fieldName);
			if (childNode != null) {
				Double val = childNode.asDouble();
				if (val != null) {
					return val;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * This function is responsible for retrieving a child json node as a
	 * {@code Map<String, Object>} from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return {@code Double} value
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapValue(JsonNode node,
			String fieldName, Map<String, Object> defaultValue) {
		if (node != null) {
			JsonNode childNode = node.get(fieldName);
			if (childNode != null) {
				return new ObjectMapper().convertValue(childNode, Map.class);
			}
		}
		return defaultValue;
	}

	/**
	 * This function is responsible for retrieving a child json node as a
	 * {@code LinkedList<Map<String, Object>>} from parent json node
	 * 
	 * @param node
	 *            Parent JSON node
	 * @param fieldName
	 * @param defaultValue
	 * @return {@code Double} value
	 */
	@SuppressWarnings("unchecked")
	public static LinkedList<Map<String, Object>> getListofMapsValue(
			JsonNode node, String fieldName,
			LinkedList<Map<String, Object>> defaultValue) {
		if (node != null) {
			ArrayNode childNode = (ArrayNode) node.get(fieldName);
			if (childNode != null) {
				ObjectMapper mapper = new ObjectMapper();
				LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
				for (JsonNode jsonNode : childNode) {
					list.add(mapper.convertValue(jsonNode, Map.class));
				}
				return list;
			}
		}
		return defaultValue;
	}

}

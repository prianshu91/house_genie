package com.promelle.object;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.promelle.exception.TransformationException;
import com.promelle.utils.JsonUtils;

/**
 * This class is base class for all entities.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractObject implements Serializable {
	private static final long serialVersionUID = 187832338L;

	/**
	 * This function is responsible for updating field in map
	 * 
	 * @param methodName
	 * @param method
	 * @param fields
	 * @param map
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void updateFieldInMap(String methodName, Method method,
			List<String> fields, Map<String, Object> map)
			throws IllegalAccessException, InvocationTargetException {
		String field = methodName.substring(0, 1).toLowerCase()
				+ methodName.substring(1);
		if (fields.contains(field)) {
			method.setAccessible(true);
			Object obj = method.invoke(this);
			if (obj == null) {
				return;
			}
			map.put(field, obj);
		} else {
			field = methodName.substring(0, 1) + methodName.substring(1);
			if (fields.contains(field)) {
				Object obj = method.invoke(this);
				if (obj == null) {
					return;
				}
				map.put(field, obj);
			}
		}
	}

	/**
	 * This function is responsible for updating fields in map
	 * 
	 * @param clazz
	 * @param fields
	 * @param map
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void updateFieldsInMap(Class clazz, List<String> fields,
			Map<String, Object> map) throws IllegalAccessException,
			InvocationTargetException {
		for (Method method : clazz.getDeclaredMethods()) {
			String methodName = method.getName();
			if (methodName.startsWith("get")) {
				methodName = methodName.substring(3);
			} else if (methodName.startsWith("is")) {
				methodName = methodName.substring(2);
			} else {
				continue;
			}
			updateFieldInMap(methodName, method, fields, map);
		}
	}

	/**
	 * This function is responsible for converting current object to map
	 * 
	 * @return map
	 * @throws TransformationException
	 */
	public Map<String, Object> toMap() throws TransformationException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Class clazz = this.getClass();
			while (clazz != null) {
				List<String> fields = getFields(clazz);
				updateFieldsInMap(clazz, fields, map);
				clazz = clazz.getSuperclass();
			}
		} catch (Exception e) {
			throw new TransformationException(e);
		}
		return map;
	}

	@Override
	public String toString() {
		try {
			return JsonUtils.getJsonString(toMap());
		} catch (TransformationException e) {
			return "{}";
		}
	}

	/**
	 * This function is responsible for populating specified object info current
	 * object
	 * 
	 * @param obj
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void populate(Object obj) throws IllegalAccessException,
			InvocationTargetException {
		BeanUtils.copyProperties(obj, this);
	}

	/**
	 * This function is responsible for providing fields of a class
	 * 
	 * @param clazz
	 * @return fields
	 */
	public List<String> getFields(Class clazz) {
		List<String> fields = new ArrayList<String>();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(JsonIgnore.class)) {
				continue;
			}
			fields.add(field.getName());
		}
		return fields;
	}


}

package com.promelle.query;

import com.promelle.dto.AbstractDTO;

/**
 * This class holds the attribute rule.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Attribute<T> extends AbstractDTO {
	private static final long serialVersionUID = 13245235L;

	private String fieldName;
	private T value;

	public static final String FIELD_NAME = "fieldName";
	public static final String VALUE = "value";

	public Attribute() {
	}

	public Attribute(String fieldName, T value) {
		super();
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}

package com.promelle.query;

/**
 * This class holds the attribute rule.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AttributeRule<T> extends Attribute<T> {
	private static final long serialVersionUID = 13245235L;

	private String rule;

	public static final String RULE = "rule";

	public AttributeRule() {
	}

	public AttributeRule(String fieldName, T value, String rule) {
		super(fieldName, value);
		this.rule = rule;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

}

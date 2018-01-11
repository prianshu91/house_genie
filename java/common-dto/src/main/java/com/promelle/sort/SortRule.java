package com.promelle.sort;

import com.promelle.query.AttributeRule;

/**
 * This class holds the sort rule.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class SortRule extends AttributeRule<SortOrder> {
	private static final long serialVersionUID = 13245235L;

	public SortRule(String fieldName, SortOrder value) {
		super(fieldName, value, "SORT");
	}

}

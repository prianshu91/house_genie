package com.promelle.sort;

/**
 * This class is responsible for holding sort orders.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public enum SortOrder {
	ASC(1), DESC(-1);

	private Integer code;

	private SortOrder(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

}

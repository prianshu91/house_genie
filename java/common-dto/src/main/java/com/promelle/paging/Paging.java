package com.promelle.paging;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding paging info.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Paging extends AbstractDTO {
	private static final long serialVersionUID = 7629575261222775892L;
	private Integer offset = 0;
	private Integer limit = 10;

	public Paging() {
		// default constructor
	}

	public Paging(Integer offset, Integer limit) {
		super();
		setOffset(offset);
		setLimit(limit);
	}

	public Paging(String offset, String limit) {
		super();
		try {
			setOffset(Integer.parseInt(offset));
		} catch (Exception e) {
			// ignore silently
		}
		try {
			setLimit(Integer.parseInt(limit));
		} catch (Exception e) {
			// ignore silently
		}
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		if (offset != null) {
			this.offset = offset;
		}
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		if (limit != null) {
			this.limit = limit;
		}
	}

	public boolean isNotReqd() {
		return offset == -1 && limit == -1;
	}

}

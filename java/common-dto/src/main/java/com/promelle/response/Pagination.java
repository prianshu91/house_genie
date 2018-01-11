package com.promelle.response;

import com.promelle.paging.Paging;

/**
 * This class is intended for holding paging info.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Pagination extends Paging {
	private static final long serialVersionUID = 17897L;
	private long count;
	private long total;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}

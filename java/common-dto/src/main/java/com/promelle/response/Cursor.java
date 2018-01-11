package com.promelle.response;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding cursor info.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Cursor extends AbstractDTO {
	private static final long serialVersionUID = 174897L;
	private int count;
	private String maxId;
	private String sinceId;
	private String previousURL;
	private String nextURL;

	public Cursor() {
	}

	public Cursor(long maxId, long sinceId) {
		this.maxId = String.valueOf(maxId);
		this.sinceId = String.valueOf(sinceId);
	}

	public Cursor(String maxId, String sinceId) {
		this.maxId = maxId;
		this.sinceId = sinceId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMaxId() {
		return maxId;
	}

	public long getMaxIdInLong() {
		try {
			return Long.parseLong(this.maxId);
		} catch (Exception e) {
			return 0L;
		}
	}

	public void setMaxId(String maxId) {
		this.maxId = maxId;
	}

	public void setMaxId(long maxId) {
		this.maxId = String.valueOf(maxId);
	}

	public String getSinceId() {
		return sinceId;
	}

	public long getSinceIdInLong() {
		try {
			return Long.parseLong(this.sinceId);
		} catch (Exception e) {
			return 0;
		}
	}

	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
	}

	public void setSinceId(long sinceId) {
		this.sinceId = String.valueOf(sinceId);
	}

	public String getPreviousURL() {
		return previousURL;
	}

	public void setPreviousURL(String previousURL) {
		this.previousURL = previousURL;
	}

	public String getNextURL() {
		return nextURL;
	}

	public void setNextURL(String nextURL) {
		this.nextURL = nextURL;
	}

}

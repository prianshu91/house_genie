package com.promelle.range;

import java.util.Date;

import com.promelle.dto.AbstractDTO;
import com.promelle.utils.DateUtils;

/**
 * This class is responsible for holding date filter.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DateRange extends AbstractDTO {
	private static final long serialVersionUID = -6138102198188513597L;

	private String start;
	private String end;

	public DateRange() {
	}

	public DateRange(String start, String end) {
		this.start = start;
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isValid() {
		return !(this.start.equals("-1") && this.end.equals("-1"));
	}

	public TimestampRange getTimestampRange() {
		TimestampRange range = new TimestampRange();
		Date stDate = DateUtils.parseDate(getStart());
		if (stDate != null) {
			range.setStart(stDate.getTime());
		}
		Date enDate = DateUtils.parseDate(getEnd());
		if (enDate != null) {
			range.setEnd(enDate.getTime() + (24 * 60 * 60 * 1000));
		}
		return range;
	}

}

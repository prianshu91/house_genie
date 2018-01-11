package com.promelle.range;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.dto.AbstractDTO;

/**
 * This class is responsible for holding timestamp range.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class TimestampRange extends AbstractDTO {
	private static final long serialVersionUID = 6960630774806049861L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TimestampRange.class.getName());

	private long start;
	private long end;

	public TimestampRange() {
	}

	public TimestampRange(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public TimestampRange(String start, String end) {
		try {
			this.start = Long.parseLong(start);
		} catch (Exception e) {
			this.start = -1;
		}
		try {
			this.end = Long.parseLong(end);
		} catch (Exception e) {
			this.end = -1;
		}
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public boolean isValid() {
		return !(start == -1 && end == -1);
	}

	public static TimestampRange getTimestampRange(String startDate,
			String endDate, String startTimestamp, String endTimestamp) {
		TimestampRange range = null;
		if (StringUtils.isNotBlank(startDate)
				&& StringUtils.isNotBlank(endDate)) {
			range = "-1".equalsIgnoreCase(startDate)
					&& "-1".equalsIgnoreCase(endDate) ? new TimestampRange(-1,
					-1) : new DateRange(startDate, endDate).getTimestampRange();
		} else if (StringUtils.isNotBlank(startTimestamp)
				&& StringUtils.isNotBlank(endTimestamp)) {
			range = "-1".equalsIgnoreCase(startTimestamp)
					&& "-1".equalsIgnoreCase(endTimestamp) ? new TimestampRange(
					-1, -1) : new TimestampRange(startTimestamp, endTimestamp);
		}
		return range;
	}

	private static Set<Long> getTimestamps(Date start, Date end,
			String rangeType) {
		Date startDate = start;
		Date endDate = end;
		Set<Long> timestamps = new TreeSet<Long>();
		timestamps.add(startDate.getTime());
		timestamps.add(endDate.getTime());
		while (startDate.before(endDate)) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar tempCalendar = Calendar.getInstance();
			tempCalendar.setTime(startDate);
			tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
			if (tempCalendar.after(endDate)) {
				break;
			}
			if ("DAILY".equalsIgnoreCase(rangeType)) {
				timestamps.add(startCalendar.getTimeInMillis()
						+ (24 * 60 * 60 * 1000));
			} else if ("MONTHLY".equalsIgnoreCase(rangeType)) {
				if (tempCalendar.get(Calendar.MONTH) != startCalendar
						.get(Calendar.MONTH)) {
					timestamps.add(startCalendar.getTimeInMillis()
							+ (24 * 60 * 60 * 1000));
				}
			} else if ("WEEKLY".equalsIgnoreCase(rangeType)) {
				if (tempCalendar.get(Calendar.WEEK_OF_YEAR) != startCalendar
						.get(Calendar.WEEK_OF_YEAR)) {
					timestamps.add(startCalendar.getTimeInMillis()
							+ (24 * 60 * 60 * 1000));
				}
			} else if ("YEARLY".equalsIgnoreCase(rangeType)) {
				if (tempCalendar.get(Calendar.YEAR) != startCalendar
						.get(Calendar.YEAR)) {
					timestamps.add(startCalendar.getTimeInMillis()
							+ (24 * 60 * 60 * 1000));
				}
			} else {
				LOGGER.debug("Invalid range type : " + rangeType);
			}
			startDate = tempCalendar.getTime();
		}
		return timestamps;
	}

	public static Map<String, TimestampRange> getTimestampRanges(Date start,
			Date end, String rangeType) {
		Date startDate = start;
		Date endDate = end;
		Map<String, TimestampRange> ranges = new LinkedHashMap<String, TimestampRange>();
		Set<Long> timestamps = getTimestamps(startDate, endDate, rangeType);
		Iterator<Long> iterator = timestamps.iterator();
		Long previous = iterator.next();
		Calendar calendar = Calendar.getInstance();
		while (iterator.hasNext()) {
			Long current = iterator.next();
			if (!previous.equals(current)) {
				TimestampRange rangeFilter = new TimestampRange(previous,
						current);
				if ("DAILY".equalsIgnoreCase(rangeType)) {
					ranges.put(
							new SimpleDateFormat("yyyy-MM-dd").format(current),
							rangeFilter);
				} else if ("MONTHLY".equalsIgnoreCase(rangeType)) {
					ranges.put(new SimpleDateFormat("yyyy-MM").format(current),
							rangeFilter);
				} else if ("WEEKLY".equalsIgnoreCase(rangeType)) {
					calendar.setTimeInMillis(current);
					ranges.put(
							String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)),
							rangeFilter);
				} else if ("YEARLY".equalsIgnoreCase(rangeType)) {
					ranges.put(new SimpleDateFormat("yyyy").format(current),
							rangeFilter);
				}
			}
			previous = current + 1000;
		}
		return ranges;
	}

}

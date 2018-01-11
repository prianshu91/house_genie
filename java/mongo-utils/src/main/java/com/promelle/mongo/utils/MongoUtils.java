package com.promelle.mongo.utils;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.utils.ListUtils;
import com.promelle.utils.UUIDUtils;

/**
 * This class is responsible for providing mongo utility functions.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public final class MongoUtils {

	private MongoUtils() {
		// default constructor
	}

	public static void appendToObj(DBObject obj, String field, Object val) {
		if (val != null) {
			obj.put(field, val);
		}
	}

	public static void appendToObjMulti(DBObject obj, String field, String value) {
		if (StringUtils.isNotBlank(value)) {
			obj.put(field, value.contains(",") ? new BasicDBObject("$in",
					ListUtils.splitStringIntoTrimmedList(value, ",")) : value);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void appendListToObj(DBObject obj, String field, List vals) {
		if (CollectionUtils.isNotEmpty(vals)) {
			obj.put(field, vals);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<String> getStringListFromObj(DBObject obj, String field) {
		if (obj.containsField(field)) {
			return (List<String>) obj.get(field);
		}
		return null;
	}

	public static void preProcessForAudit(
			AbstractRequestTracker requestTracker, AbstractAuditDTO b) {
		if (StringUtils.isEmpty(b.getId())) {
			b.setId(UUIDUtils.getUUID());
			if (b.getStatus() == null) {
				b.setStatus(1);
			}
		}
		if (b.getCreatedOn() == null) {
			b.setCreatedOn(new Date().getTime());
		}
		if (b.getModifiedOn() == null) {
			b.setModifiedOn(new Date().getTime());
		}
		if (b.getCreatedBy() == null) {
			b.setCreatedBy(requestTracker.getRequestBy());
		}
		if (b.getModifiedBy() == null) {
			b.setModifiedBy(requestTracker.getRequestBy());
		}
	}

}

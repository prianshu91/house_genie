package com.promelle.ui.dao.mapper;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBObject;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.ui.dto.LoginSession;

/**
 * This class is intended for providing mongo mapping.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class LoginSessionMongoMapper implements
		MongoMapper<LoginSession, SearchFilter> {

	public static final String MODIFIED_ON = "modifiedOn";
	public static final String TIME_ZONE = "timeZone";
	public static final String RESPONSE = "response";
	public static final String PERMISSIONS = "permissions";
	public static final String USER_ID = "userId";
	public static final String CREATED_ON = "createdOn";

	@Override
	public BasicDBObject convertToDao(LoginSession dto) {
		if (dto == null) {
			return null;
		}
		BasicDBObject obj = new BasicDBObject();
		if (StringUtils.isNotBlank(dto.getId())) {
			obj.put(AbstractMongoDao.MONGO_ID, dto.getId());
		}
		if (dto.getUserId() != null) {
			obj.put(USER_ID, dto.getUserId());
		}
		if (dto.getCreatedOn() != null) {
			obj.put(CREATED_ON, dto.getCreatedOn());
		}
		if (dto.getModifiedOn() != null) {
			obj.put(MODIFIED_ON, dto.getModifiedOn());
		}
		if (CollectionUtils.isNotEmpty(dto.getPermissions())) {
			obj.put(PERMISSIONS, dto.getPermissions());
		}
		if (dto.getResponse() != null) {
			obj.put(RESPONSE, dto.getResponse());
		}
		if (dto.getTimeZone() != null) {
			obj.put(TIME_ZONE, dto.getTimeZone());
		}
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(SearchFilter filter) {
		if (filter == null) {
			return null;
		}
		BasicDBObject obj = new BasicDBObject();
		if (StringUtils.isNotBlank(filter.getId())) {
			obj.put(AbstractMongoDao.MONGO_ID, filter.getId());
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LoginSession convertToDto(BasicDBObject obj) {
		if (obj == null) {
			return null;
		}
		LoginSession session = new LoginSession();
		if (obj.containsField(AbstractMongoDao.MONGO_ID)) {
			session.setId(String.valueOf(obj.get(AbstractMongoDao.MONGO_ID)));
		}
		if (obj.containsField(USER_ID)) {
			session.setUserId((String) obj.get(USER_ID));
		}
		if (obj.containsField(CREATED_ON)) {
			session.setCreatedOn((Long) obj.get(CREATED_ON));
		}
		if (obj.containsField(MODIFIED_ON)) {
			session.setModifiedOn((Long) obj.get(MODIFIED_ON));
		}
		if (obj.containsField(PERMISSIONS)) {
			session.setPermissions((List<String>) obj.get(PERMISSIONS));
		}
		if (obj.containsField(RESPONSE)) {
			session.setResponse((String) obj.get(RESPONSE));
		}
		if (obj.containsField(TIME_ZONE)) {
			session.setTimeZone((String) obj.get(TIME_ZONE));
		}
		return session;
	}

}

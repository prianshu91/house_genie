package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.OrderAddress;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.OrderAddressFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderAddressMapper implements
		MongoMapper<OrderAddress, OrderAddressFilter> {
	public static final String ORDER_ID = "orderId";
	public static final String USER_ID = "userId";
	public static final String NAME = "name";
	public static final String MOBILE = "mobile";
	public static final String ADDRESS_LINE1 = "addressLine1";
	public static final String ADDRESS_LINE2 = "addressLine2";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP_CODE = "zipCode";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	@Override
	public BasicDBObject convertToDao(OrderAddress dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, NAME, dto.getName());
		MongoUtils.appendToObj(obj, MOBILE, dto.getMobile());
		MongoUtils.appendToObj(obj, ADDRESS_LINE1, dto.getAddressLine1());
		MongoUtils.appendToObj(obj, ADDRESS_LINE2, dto.getAddressLine2());
		MongoUtils.appendToObj(obj, CITY, dto.getCity());
		MongoUtils.appendToObj(obj, STATE, dto.getState());
		MongoUtils.appendToObj(obj, ZIP_CODE, dto.getZipCode());
		MongoUtils.appendToObj(obj, LATITUDE, dto.getLatitude());
		MongoUtils.appendToObj(obj, LONGITUDE, dto.getLongitude());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(OrderAddressFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, ORDER_ID, filter.getOrderId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		return obj;
	}

	@Override
	public OrderAddress convertToDto(BasicDBObject obj) {
		OrderAddress dto = new OrderAddress();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setOrderId(obj.getString(ORDER_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setName(obj.getString(NAME));
		dto.setMobile(obj.getString(MOBILE));
		dto.setAddressLine1(obj.getString(ADDRESS_LINE1));
		dto.setAddressLine2(obj.getString(ADDRESS_LINE2));
		dto.setCity(obj.getString(CITY));
		dto.setState(obj.getString(STATE));
		dto.setZipCode(obj.getString(ZIP_CODE));
		if (obj.containsField(LATITUDE)) {
			dto.setLatitude(obj.getDouble(LATITUDE));
		}
		if (obj.containsField(LONGITUDE)) {
			dto.setLongitude(obj.getDouble(LONGITUDE));
		}
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		if (obj.containsField(MODIFIED_ON)) {
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		dto.setModifiedBy(obj.getString(MODIFIED_BY));
		if (obj.containsField(STATUS)) {
			dto.setStatus(obj.getInt(STATUS));
		}
		return dto;
	}

}

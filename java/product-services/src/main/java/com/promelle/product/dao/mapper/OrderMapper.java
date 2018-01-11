package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.promelle.constants.Punctuation;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.constants.OrderStatus;
import com.promelle.product.dto.Order;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.OrderFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderMapper implements MongoMapper<Order, OrderFilter> {
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String ORDER_STATUS = "orderStatus";
	public static final String TRACK = "track";

	@Override
	public BasicDBObject convertToDao(Order dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, USER_NAME, dto.getUserName());
		MongoUtils.appendToObj(obj, ORDER_STATUS, dto.getOrderStatus());
		if (dto.getTrack() != null && !dto.getTrack().isEmpty()) {
			for (Entry<String, Long> entry : dto.getTrack().entrySet()) {
				obj.put(TRACK + Punctuation.DOT + entry.getKey(),
						entry.getValue());
			}
		}
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(OrderFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		if (filter.getOrderStatus() == null) {
			if (filter.getExcludePending() != null
					&& filter.getExcludePending()) {
				obj.put(ORDER_STATUS, new BasicDBObject("$ne",
						OrderStatus.ORDER_PENDING.name()));
			}
		} else {
			MongoUtils.appendToObjMulti(obj, ORDER_STATUS,
					filter.getOrderStatus());
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Order convertToDto(BasicDBObject obj) {
		Order dto = new Order();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setUserName(obj.getString(USER_NAME));
		dto.setOrderStatus(obj.getString(ORDER_STATUS));
		if (obj.containsField(TRACK)) {
			dto.setTrack((Map<String, Long>) obj.get(TRACK));
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

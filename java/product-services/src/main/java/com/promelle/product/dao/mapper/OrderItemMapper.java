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
import com.promelle.product.dto.OrderItem;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.OrderItemFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderItemMapper implements MongoMapper<OrderItem, OrderItemFilter> {
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String PRODUCT_ID = "productId";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String PRICE = "price";
	public static final String ORDER_ID = "orderId";
	public static final String SHIPMENT_ID = "shipmentId";

	@Override
	public BasicDBObject convertToDao(OrderItem dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, USER_NAME, dto.getUserName());
		MongoUtils.appendToObj(obj, PRODUCT_ID, dto.getProductId());
		MongoUtils.appendToObj(obj, START_DATE, dto.getStartDate());
		MongoUtils.appendToObj(obj, END_DATE, dto.getEndDate());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, PRICE, dto.getPrice());
		MongoUtils.appendToObj(obj, SHIPMENT_ID, dto.getShipmentId());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(OrderItemFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		MongoUtils.appendToObj(obj, ORDER_ID, filter.getOrderId());
		MongoUtils.appendToObj(obj, PRODUCT_ID, filter.getProductId());
		MongoUtils.appendToObj(obj, SHIPMENT_ID, filter.getShipmentId());
		if (filter.getIsShipmentExists() != null
				&& filter.getIsShipmentExists()) {
			obj.put(SHIPMENT_ID, new BasicDBObject("$exists", true));
		}
		return obj;
	}

	@Override
	public OrderItem convertToDto(BasicDBObject obj) {
		OrderItem dto = new OrderItem();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setUserName(obj.getString(USER_NAME));
		dto.setProductId(obj.getString(PRODUCT_ID));
		if (obj.containsField(START_DATE)) {
			dto.setStartDate(obj.getLong(START_DATE));
		}
		if (obj.containsField(END_DATE)) {
			dto.setEndDate(obj.getLong(END_DATE));
		}
		dto.setOrderId(obj.getString(ORDER_ID));
		if (obj.containsField(PRICE)) {
			dto.setPrice(obj.getDouble(PRICE));
		}
		if (obj.containsField(SHIPMENT_ID)) {
			dto.setShipmentId(obj.getString(SHIPMENT_ID));
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

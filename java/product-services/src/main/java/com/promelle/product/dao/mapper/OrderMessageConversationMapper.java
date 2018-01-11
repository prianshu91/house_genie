package com.promelle.product.dao.mapper;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.OrderMessageConversation;
import com.promelle.product.filter.OrderMessageConversationFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link OrderMessageConversation}.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class OrderMessageConversationMapper implements
		MongoMapper<OrderMessageConversation, OrderMessageConversationFilter> {
	public static final String SENDER_ID = "senderId";
	public static final String RECEIVER_ID = "receiverId";
	public static final String ORDER_ID = "orderId";

	@Override
	public BasicDBObject convertToDao(OrderMessageConversation dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, SENDER_ID, dto.getSenderId());
		MongoUtils.appendToObj(obj, RECEIVER_ID, dto.getReceiverId());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(OrderMessageConversationFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		return obj;
	}

	@Override
	public OrderMessageConversation convertToDto(BasicDBObject obj) {
		OrderMessageConversation dto = new OrderMessageConversation();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setSenderId(obj.getString(SENDER_ID));
		dto.setReceiverId(obj.getString(RECEIVER_ID));
		dto.setOrderId(obj.getString(ORDER_ID));
		return dto;
	}

}

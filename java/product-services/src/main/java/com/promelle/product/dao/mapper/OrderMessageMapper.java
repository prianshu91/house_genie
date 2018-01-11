package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;

import java.sql.ResultSet;

import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.OrderMessage;
import com.promelle.product.filter.OrderMessageFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link OrderMessage}.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class OrderMessageMapper implements
		MongoMapper<OrderMessage, OrderMessageFilter> {

	public static final String TEXT = "text";
	public static final String CONVERSATION_ID = "conversationId";
	public static final String ORDER_ID = "orderId";
	public static final String SENDER_ID = "senderId";
	public static final String SENDER_NAME = "senderName";
	public static final String RECEIVER_ID = "receiverId";
	public static final String RECEIVER_NAME = "receiverName";
	public static final String TIMESTAMP = "timestamp";
	public static final String READ = "read";
	public static final String UNREAD_COUNT = "unreadCount";

	@Override
	public BasicDBObject convertToDao(OrderMessage dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, TEXT, dto.getText());
		MongoUtils.appendToObj(obj, CONVERSATION_ID, dto.getConversationId());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, SENDER_ID, dto.getSenderId());
		MongoUtils.appendToObj(obj, SENDER_NAME, dto.getSenderName());
		MongoUtils.appendToObj(obj, RECEIVER_ID, dto.getReceiverId());
		MongoUtils.appendToObj(obj, RECEIVER_NAME, dto.getReceiverName());
		MongoUtils.appendToObj(obj, TIMESTAMP, dto.getTimestamp());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		if (dto.isNew()) {
			MongoUtils.appendToObj(obj, dto.getSenderId(), 1);
			MongoUtils.appendToObj(obj, dto.getReceiverId(), 1);
		}
		obj.put(READ, dto.isRead());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(OrderMessageFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, ORDER_ID, filter.getOrderId());
		if (filter.getIsConversation() != null && filter.getIsConversation()) {
			BasicDBList orList = new BasicDBList();
			orList.add(new BasicDBObject(SENDER_ID, filter.getSenderId())
					.append(RECEIVER_ID, filter.getReceiverId()));
			orList.add(new BasicDBObject(RECEIVER_ID, filter.getSenderId())
					.append(SENDER_ID, filter.getReceiverId()));
			obj.put("$or", orList);
		} else {
			MongoUtils.appendToObj(obj, SENDER_ID, filter.getSenderId());
			MongoUtils.appendToObj(obj, RECEIVER_ID, filter.getReceiverId());
		}
		MongoUtils
				.appendToObj(obj, CONVERSATION_ID, filter.getConversationId());
		if (StringUtils.isNotBlank(filter.getReceiverId())) {
			MongoUtils.appendToObj(obj, filter.getReceiverId(), 1);
		}
		return obj;
	}

	@Override
	public OrderMessage convertToDto(BasicDBObject obj) {
		OrderMessage dto = new OrderMessage();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setText(obj.getString(TEXT));
		dto.setConversationId(obj.getString(CONVERSATION_ID));
		dto.setOrderId(obj.getString(ORDER_ID));
		dto.setSenderId(obj.getString(SENDER_ID));
		dto.setSenderName(obj.getString(SENDER_NAME));
		dto.setReceiverId(obj.getString(RECEIVER_ID));
		dto.setReceiverName(obj.getString(RECEIVER_NAME));
		if (obj.containsField(TIMESTAMP)) {
			dto.setTimestamp(obj.getLong(TIMESTAMP));
		}
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		if (obj.containsField(MODIFIED_ON)) {
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		dto.setModifiedBy(obj.getString(MODIFIED_BY));
		if (obj.containsField(READ)) {
			dto.setRead((Boolean) obj.get(READ));
		}
		if (obj.containsField(UNREAD_COUNT)) {
			dto.setUnreadCount((Integer) obj.get(UNREAD_COUNT));
		}
		return dto;
	}

}

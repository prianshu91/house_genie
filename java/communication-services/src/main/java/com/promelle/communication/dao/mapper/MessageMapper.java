package com.promelle.communication.dao.mapper;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.communication.dto.Message;
import com.promelle.communication.filter.MessageFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class MessageMapper implements MongoMapper<Message, MessageFilter> {
	public static final String TEXT = "text";
	public static final String SENDER_ID = "senderId";
	public static final String SENDER_NAME = "senderName";
	public static final String RECEIVER_ID = "receiverId";
	public static final String RECEIVER_NAME = "receiverName";
	public static final String TIMESTAMP = "timestamp";

	@Override
	public BasicDBObject convertToDao(Message dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, TEXT, dto.getText());
		MongoUtils.appendToObj(obj, SENDER_ID, dto.getSenderId());
		MongoUtils.appendToObj(obj, SENDER_NAME, dto.getSenderName());
		MongoUtils.appendToObj(obj, RECEIVER_ID, dto.getReceiverId());
		MongoUtils.appendToObj(obj, RECEIVER_NAME, dto.getReceiverName());
		MongoUtils.appendToObj(obj, TIMESTAMP, dto.getTimestamp());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(MessageFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, SENDER_ID, filter.getSenderId());
		MongoUtils.appendToObj(obj, RECEIVER_ID, filter.getReceiverId());
		return obj;
	}

	@Override
	public Message convertToDto(BasicDBObject obj) {
		Message dto = new Message();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setText(obj.getString(TEXT));
		dto.setSenderId(obj.getString(SENDER_ID));
		dto.setSenderName(obj.getString(SENDER_NAME));
		dto.setReceiverId(obj.getString(RECEIVER_ID));
		dto.setReceiverName(obj.getString(RECEIVER_NAME));
		if (obj.containsField(TIMESTAMP)) {
			dto.setTimestamp(obj.getLong(TIMESTAMP));
		}
		return dto;
	}

}

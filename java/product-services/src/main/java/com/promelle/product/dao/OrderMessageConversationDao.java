package com.promelle.product.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.OrderMessageConversationMapper;
import com.promelle.product.dto.OrderMessageConversation;
import com.promelle.product.filter.OrderMessageConversationFilter;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.utils.UUIDUtils;

/**
 * This interface is intended for providing interactions with order message
 * conversation table.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class OrderMessageConversationDao
		extends
		AbstractMongoDao<OrderMessageConversation, OrderMessageConversationFilter, OrderMessageConversationMapper> {

	public OrderMessageConversationDao(MongoManager mongoManager)
			throws AbstractException {
		super(mongoManager, "orderMessageConversation",
				OrderMessageConversationMapper.class);
	}

	/**
	 * Get order Message conversation ID
	 * 
	 * @param requestTracker
	 * @param senderId
	 * @param receiverId
	 * @param orderId
	 * @return already exists conversation ID otherwise make a new entry
	 * @throws AbstractException
	 */
	public String getConversationId(AbstractRequestTracker requestTracker,
			String senderId, String receiverId, String orderId)
			throws AbstractException {

		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject(OrderMessageConversationMapper.RECEIVER_ID,
				receiverId).append(OrderMessageConversationMapper.SENDER_ID,
				senderId));
		list.add(new BasicDBObject(OrderMessageConversationMapper.RECEIVER_ID,
				senderId).append(OrderMessageConversationMapper.SENDER_ID,
				receiverId));

		DBObject findObj = getCollection().findOne(
				new BasicDBObject(OrderMessageConversationMapper.ORDER_ID,
						orderId).append("$or", list));
		if (findObj != null) {
			return (String) findObj.get(MONGO_ID);
		}
		
		OrderMessageConversation conversation = new OrderMessageConversation();
		conversation.setSenderId(senderId);
		conversation.setReceiverId(receiverId);
		conversation.setOrderId(orderId);
		conversation.setId(UUIDUtils.getUUID());
		save(requestTracker, conversation);
		return conversation.getId();
	}
}

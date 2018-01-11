package com.promelle.product.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.OrderMessageMapper;
import com.promelle.product.dto.OrderMessage;
import com.promelle.product.filter.OrderMessageFilter;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;

/**
 * This interface is intended for providing interactions with order messages
 * table.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class OrderMessageDao extends
		AbstractMongoDao<OrderMessage, OrderMessageFilter, OrderMessageMapper> {

	public OrderMessageDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "orderMessages", OrderMessageMapper.class);
	}

	/**
	 * Get User's order messages list
	 * 
	 * @param userId
	 * @return list or orderMessages
	 * @throws AbstractException
	 */
	public PagedList<OrderMessage> getUserInbox(String userId)
			throws AbstractException {
		List<DBObject> pipeline = new ArrayList<DBObject>();

		// match
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject(OrderMessageMapper.RECEIVER_ID, userId));
		list.add(new BasicDBObject(OrderMessageMapper.SENDER_ID, userId));
		pipeline.add(new BasicDBObject("$match", new BasicDBObject(userId, 1)
				.append("$or", list)));

		// sort
		pipeline.add(new BasicDBObject("$sort", new BasicDBObject(
				OrderMessageMapper.TIMESTAMP, -1)));

		// GROUPBY
		DBObject groupObj = new BasicDBObject();
		groupObj.put(MONGO_ID, "$conversationId");
		groupObj.put(OrderMessageMapper.TEXT, new BasicDBObject("$first",
				"$text"));
		groupObj.put(OrderMessageMapper.ORDER_ID, new BasicDBObject("$first",
				"$orderId"));
		groupObj.put(OrderMessageMapper.SENDER_ID, new BasicDBObject("$first",
				"$senderId"));
		groupObj.put(OrderMessageMapper.SENDER_NAME, new BasicDBObject(
				"$first", "$senderName"));
		groupObj.put(OrderMessageMapper.RECEIVER_ID, new BasicDBObject(
				"$first", "$receiverId"));
		groupObj.put(OrderMessageMapper.RECEIVER_NAME, new BasicDBObject(
				"$first", "$receiverName"));
		groupObj.put(OrderMessageMapper.TIMESTAMP, new BasicDBObject("$first",
				"$timestamp"));
		groupObj.put(OrderMessageMapper.READ, new BasicDBObject("$first",
				"$read"));
		BasicDBList countCond = new BasicDBList();

		BasicDBList andList = new BasicDBList();
		andList.add(new BasicDBObject("$eq", Arrays.asList("$receiverId",
				userId)));
		andList.add(new BasicDBObject("$eq", Arrays.asList("$read", false)));

		countCond.add(new BasicDBObject("$and", andList));
		countCond.add(1);
		countCond.add(0);
		groupObj.put(OrderMessageMapper.UNREAD_COUNT, new BasicDBObject("$sum",
				new BasicDBObject("$cond", countCond)));
		pipeline.add(new BasicDBObject("$group", groupObj));
		pipeline.add(new BasicDBObject("$sort", new BasicDBObject(
				OrderMessageMapper.TIMESTAMP, -1)));
		AggregationOutput output = getCollection().aggregate(pipeline);
		Iterator<DBObject> it = output.results().iterator();

		List<OrderMessage> messages = new ArrayList<OrderMessage>();
		while (it.hasNext()) {
			messages.add(getMapper().convertToDto((BasicDBObject) it.next()));
		}
		PagedList<OrderMessage> pagedList = new PagedList<OrderMessage>();
		pagedList.setObjects(messages);
		Pagination pagination = new Pagination();
		pagination.setOffset(0);
		pagination.setLimit(messages.size());
		pagination.setCount(messages.size());
		pagination.setTotal(messages.size());
		pagedList.setPagination(pagination);

		return pagedList;
	}

	/**
	 * Delete conversation bet receiver and sender
	 * 
	 * @param requestTracker
	 * @param conversationId
	 * @param userId
	 * @return 0 or 1
	 */
	public int softDeleteConversation(AbstractRequestTracker requestTracker,
			String conversationId, String userId) {

		return getCollection().update(
				new BasicDBObject(OrderMessageMapper.CONVERSATION_ID,
						conversationId),
				new BasicDBObject(SET_OPERATOR, new BasicDBObject(userId, 0)),
				false, true).getN();
	}

	/**
	 * Delete order messages
	 * 
	 * @param requestTracker
	 * @param messageId
	 * @param userId
	 * @return 0 or 1
	 */
	public int softDeleteMessage(AbstractRequestTracker requestTracker,
			String messageId, String userId) {
		return getCollection().update(
				new BasicDBObject(AbstractMongoDao.MONGO_ID, messageId),
				new BasicDBObject(SET_OPERATOR, new BasicDBObject(userId, 0)),
				false, true).getN();
	}

}

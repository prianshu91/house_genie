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
import com.promelle.product.dao.mapper.ProductMessageMapper;
import com.promelle.product.dto.ProductMessage;
import com.promelle.product.filter.ProductMessageFilter;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;

/**
 * This interface is intended for providing interactions with messages table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductMessageDao
		extends
		AbstractMongoDao<ProductMessage, ProductMessageFilter, ProductMessageMapper> {

	public ProductMessageDao(MongoManager mongoManager)
			throws AbstractException {
		super(mongoManager, "productMessages", ProductMessageMapper.class);
	}

	public PagedList<ProductMessage> getUserInbox(String userId)
			throws AbstractException {
		List<DBObject> pipeline = new ArrayList<DBObject>();

		// match
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject(ProductMessageMapper.RECEIVER_ID, userId));
		list.add(new BasicDBObject(ProductMessageMapper.SENDER_ID, userId));
		pipeline.add(new BasicDBObject("$match", new BasicDBObject(userId, 1)
				.append("$or", list)));

		// sort
		pipeline.add(new BasicDBObject("$sort", new BasicDBObject(
				ProductMessageMapper.TIMESTAMP, -1)));

		DBObject groupObj = new BasicDBObject();
		groupObj.put("_id", "$conversationId");
		groupObj.put("text", new BasicDBObject("$first", "$text"));
		groupObj.put("productId", new BasicDBObject("$first", "$productId"));
		groupObj.put("productTitle", new BasicDBObject("$first",
				"$productTitle"));
		groupObj.put("senderId", new BasicDBObject("$first", "$senderId"));
		groupObj.put("senderName", new BasicDBObject("$first", "$senderName"));
		groupObj.put("receiverId", new BasicDBObject("$first", "$receiverId"));
		groupObj.put("receiverName", new BasicDBObject("$first",
				"$receiverName"));
		groupObj.put("timestamp", new BasicDBObject("$first", "$timestamp"));
		groupObj.put("read", new BasicDBObject("$first", "$read"));
		BasicDBList countCond = new BasicDBList();

		BasicDBList andList = new BasicDBList();
		andList.add(new BasicDBObject("$eq", Arrays.asList("$receiverId",
				userId)));
		andList.add(new BasicDBObject("$eq", Arrays.asList("$read", false)));

		countCond.add(new BasicDBObject("$and", andList));
		countCond.add(1);
		countCond.add(0);
		groupObj.put("unreadCount", new BasicDBObject("$sum",
				new BasicDBObject("$cond", countCond)));
		pipeline.add(new BasicDBObject("$group", groupObj));
		pipeline.add(new BasicDBObject("$sort", new BasicDBObject("timestamp",
				-1)));
		AggregationOutput output = getCollection().aggregate(pipeline);
		Iterator<DBObject> it = output.results().iterator();
		List<ProductMessage> messages = new ArrayList<ProductMessage>();
		while (it.hasNext()) {
			messages.add(getMapper().convertToDto((BasicDBObject) it.next()));
		}
		PagedList<ProductMessage> pagedList = new PagedList<ProductMessage>();
		pagedList.setObjects(messages);
		Pagination pagination = new Pagination();
		pagination.setOffset(0);
		pagination.setLimit(messages.size());
		pagination.setCount(messages.size());
		pagination.setTotal(messages.size());
		pagedList.setPagination(pagination);
		return pagedList;
	}

	public int updateProductTitle(AbstractRequestTracker requestTracker,
			String productId, String productTitle) throws AbstractException {
		ProductMessageFilter filter = new ProductMessageFilter();
		filter.setProductId(productId);
		ProductMessage message = new ProductMessage();
		message.setProductTitle(productTitle);
		return update(requestTracker, filter, message);
	}

	public int softDeleteConversation(AbstractRequestTracker requestTracker,
			String conversationId, String userId) {
		return getCollection().update(
				new BasicDBObject(ProductMessageMapper.CONVERSATION_ID,
						conversationId),
				new BasicDBObject(SET_OPERATOR, new BasicDBObject(userId, 0)),
				false, true).getN();
	}

	public int softDeleteMessage(AbstractRequestTracker requestTracker,
			String messageId, String userId) {
		return getCollection().update(
				new BasicDBObject(AbstractMongoDao.MONGO_ID, messageId),
				new BasicDBObject(SET_OPERATOR, new BasicDBObject(userId, 0)),
				false, true).getN();
	}

}

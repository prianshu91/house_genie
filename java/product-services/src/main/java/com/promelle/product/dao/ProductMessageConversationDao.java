package com.promelle.product.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.ProductMessageConversationMapper;
import com.promelle.product.dto.ProductMessageConversation;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.utils.UUIDUtils;

/**
 * This interface is intended for providing interactions with messages table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductMessageConversationDao
		extends
		AbstractMongoDao<ProductMessageConversation, SearchFilter, ProductMessageConversationMapper> {

	public ProductMessageConversationDao(MongoManager mongoManager)
			throws AbstractException {
		super(mongoManager, "productMessageConversation",
				ProductMessageConversationMapper.class);
	}

	public String getConversationId(AbstractRequestTracker requestTracker,
			String senderId, String receiverId, String productId)
			throws AbstractException {
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject(
				ProductMessageConversationMapper.RECEIVER_ID, receiverId)
				.append(ProductMessageConversationMapper.SENDER_ID, senderId));
		list.add(new BasicDBObject(
				ProductMessageConversationMapper.RECEIVER_ID, senderId).append(
				ProductMessageConversationMapper.SENDER_ID, receiverId));
		DBObject findObj = getCollection().findOne(
				new BasicDBObject(ProductMessageConversationMapper.PRODUCT_ID,
						productId).append("$or", list));
		if (findObj != null) {
			return (String) findObj.get(MONGO_ID);
		}
		ProductMessageConversation conversation = new ProductMessageConversation();
		conversation.setSenderId(senderId);
		conversation.setReceiverId(receiverId);
		conversation.setProductId(productId);
		conversation.setId(UUIDUtils.getUUID());
		save(requestTracker, conversation);
		return conversation.getId();
	}
}

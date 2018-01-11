package com.promelle.communication.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.promelle.communication.dao.mapper.MessageMapper;
import com.promelle.communication.dto.Message;
import com.promelle.communication.filter.MessageFilter;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;

/**
 * This interface is intended for providing interactions with messages table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class MessageDao extends
		AbstractMongoDao<Message, MessageFilter, MessageMapper> {

	public MessageDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "messages", MessageMapper.class);
	}

	public PagedList<Message> getUserInbox(String userId)
			throws AbstractException {
		List<DBObject> pipeline = new ArrayList<DBObject>();
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject("receiverId", userId));
		list.add(new BasicDBObject("senderId", userId));
		pipeline.add(new BasicDBObject("$match", new BasicDBObject("$or", list)));
		pipeline.add(new BasicDBObject("$sort", new BasicDBObject("timestamp",
				-1)));
		DBObject groupObj = new BasicDBObject();
		groupObj.put("_id", "$senderId");
		groupObj.put("text", new BasicDBObject("$first", "$text"));
		groupObj.put("senderId", new BasicDBObject("$first", "$senderId"));
		groupObj.put("senderName", new BasicDBObject("$first", "$senderName"));
		groupObj.put("receiverId", new BasicDBObject("$first", "$receiverId"));
		groupObj.put("receiverName", new BasicDBObject("$first",
				"$receiverName"));
		groupObj.put("timestamp", new BasicDBObject("$first", "$timestamp"));
		pipeline.add(new BasicDBObject("$group", groupObj));
		AggregationOutput output = getCollection().aggregate(pipeline);
		Iterator<DBObject> it = output.results().iterator();
		List<Message> messages = new ArrayList<Message>();
		while (it.hasNext()) {
			messages.add(getMapper().convertToDto((BasicDBObject) it.next()));
		}
		PagedList<Message> pagedList = new PagedList<Message>();
		pagedList.setObjects(messages);
		Pagination pagination = new Pagination();
		pagination.setOffset(0);
		pagination.setLimit(messages.size());
		pagination.setCount(messages.size());
		pagination.setTotal(messages.size());
		pagedList.setPagination(pagination);
		return pagedList;
	}

	public Map<String, String> getUsers() {
		DBCursor cur = getCollection().find();
		Map<String, String> users = new HashMap<>();
		for (DBObject obj : cur) {
			users.put((String) obj.get("receiverId"),
					(String) obj.get("receiverName"));
		}
		return users;
	}

}

package com.promelle.ui.dao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mongodb.BasicDBObject;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.ui.dao.mapper.LoginSessionMongoMapper;
import com.promelle.ui.dto.LoginSession;

/**
 * This class is intended for providing mongo session.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class LoginSessionDao extends
		AbstractMongoDao<LoginSession, SearchFilter, LoginSessionMongoMapper> {

	public LoginSessionDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "loginSessions", LoginSessionMongoMapper.class);
	}

	public void updatedModifiedOn(String sessionId, long timestamp) {
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new Runnable() {
			@Override
			public void run() {
				getCollection()
						.update(new BasicDBObject(MONGO_ID, sessionId),
								new BasicDBObject("$set", new BasicDBObject(
										LoginSessionMongoMapper.MODIFIED_ON,
										timestamp)));
				executor.shutdown();
			}
		});
	}

	public void deleteById(String id) {
		getCollection().remove(new BasicDBObject(MONGO_ID, id));
	}

}

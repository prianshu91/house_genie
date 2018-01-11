package com.promelle.product.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.ReviewMapper;
import com.promelle.product.dto.Review;
import com.promelle.product.dto.ReviewStats;
import com.promelle.product.filter.ReviewFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ReviewDao extends
		AbstractMongoDao<Review, ReviewFilter, ReviewMapper> {

	public ReviewDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "productReviews", ReviewMapper.class);
	}

	public ReviewStats calculateAverageRating(String productId)
			throws AbstractException {
		List<DBObject> pipeline = new ArrayList<DBObject>();
		pipeline.add(new BasicDBObject("$match", new BasicDBObject(
				ReviewMapper.PRODUCT_ID, productId)));
		DBObject groupObj = new BasicDBObject();
		groupObj.put(AbstractMongoDao.MONGO_ID, "$" + ReviewMapper.PRODUCT_ID);
		groupObj.put("avg", new BasicDBObject("$avg", "$" + ReviewMapper.RATING));
		groupObj.put("count", new BasicDBObject("$sum", 1));
		pipeline.add(new BasicDBObject("$group", groupObj));
		AggregationOutput output = getCollection().aggregate(pipeline);
		Iterator<DBObject> it = output.results().iterator();
		ReviewStats reviewStats = null;
		if (it.hasNext()) {
			DBObject obj = (BasicDBObject) it.next();
			reviewStats = new ReviewStats();
			reviewStats.setAverageRating((Double) obj.get("avg"));
			reviewStats.setReviewCount((Integer) obj.get("count"));
		}
		return reviewStats;
	}

}

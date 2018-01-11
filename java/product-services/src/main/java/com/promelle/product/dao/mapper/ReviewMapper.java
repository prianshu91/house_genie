package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.Review;
import com.promelle.product.filter.ReviewFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ReviewMapper implements MongoMapper<Review, ReviewFilter> {
	private static final String TIMESTAMP = "timestamp";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String RATING = "rating";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String PRODUCT_ID = "productId";

	@Override
	public BasicDBObject convertToDao(Review dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, TITLE, dto.getTitle());
		MongoUtils.appendToObj(obj, DESCRIPTION, dto.getDescription());
		MongoUtils.appendToObj(obj, TIMESTAMP, dto.getTimestamp());
		MongoUtils.appendToObj(obj, RATING, dto.getRating());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, USER_NAME, dto.getUserName());
		MongoUtils.appendToObj(obj, PRODUCT_ID, dto.getProductId());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(ReviewFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		MongoUtils.appendToObj(obj, PRODUCT_ID, filter.getProductId());
		return obj;
	}

	@Override
	public Review convertToDto(BasicDBObject obj) {
		Review dto = new Review();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setTitle(obj.getString(TITLE));
		dto.setDescription(obj.getString(DESCRIPTION));
		if (obj.containsField(TIMESTAMP)) {
			dto.setTimestamp(obj.getLong(TIMESTAMP));
		}
		if (obj.containsField(RATING)) {
			dto.setRating(obj.getDouble(RATING));
		}
		dto.setUserId(obj.getString(USER_ID));
		dto.setUserName(obj.getString(USER_NAME));
		dto.setProductId(obj.getString(PRODUCT_ID));
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		if (obj.containsField(MODIFIED_ON)) {
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		dto.setModifiedBy(obj.getString(MODIFIED_BY));
		if (obj.containsField(STATUS)) {
			dto.setStatus(obj.getInt(STATUS));
		}
		return dto;
	}

}

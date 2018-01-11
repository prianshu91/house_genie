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
import com.promelle.product.dto.Favorite;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.FavoriteFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author kanak
 * @version 1.0
 */
public class FavoriteMapper implements MongoMapper<Favorite, FavoriteFilter> {
	public static final String USER_ID = "userId";
	public static final String PRODUCT_ID = "productId";
	public static final String FAVOURITE = "favourite";

	@Override
	public BasicDBObject convertToDao(Favorite dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, PRODUCT_ID, dto.getProductId());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(FavoriteFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		MongoUtils.appendToObj(obj, PRODUCT_ID, filter.getProductId());
		MongoUtils.appendToObj(obj, FAVOURITE, filter.getFavourite());
		return obj;
	}

	@Override
	public Favorite convertToDto(BasicDBObject obj) {
		Favorite dto = new Favorite();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setProductId(obj.getString(PRODUCT_ID));
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		if (obj.containsField(MODIFIED_ON)) {
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		if (obj.containsField(STATUS)) {
			dto.setStatus(obj.getInt(STATUS));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		dto.setModifiedBy(obj.getString(MODIFIED_BY));
		return dto;
	}

}

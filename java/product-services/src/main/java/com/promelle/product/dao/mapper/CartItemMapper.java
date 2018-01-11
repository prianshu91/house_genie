package com.promelle.product.dao.mapper;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.CartItem;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.CartItemFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class CartItemMapper implements MongoMapper<CartItem, CartItemFilter> {
	public static final String USER_ID = "userId";
	public static final String PRODUCT_ID = "productId";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";

	@Override
	public BasicDBObject convertToDao(CartItem dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, PRODUCT_ID, dto.getProductId());
		MongoUtils.appendToObj(obj, START_DATE, dto.getStartDate());
		MongoUtils.appendToObj(obj, END_DATE, dto.getEndDate());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(CartItemFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		return obj;
	}

	@Override
	public CartItem convertToDto(BasicDBObject obj) {
		CartItem dto = new CartItem();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setProductId(obj.getString(PRODUCT_ID));
		if (obj.containsField(START_DATE)) {
			dto.setStartDate(obj.getLong(START_DATE));
		}
		if (obj.containsField(END_DATE)) {
			dto.setEndDate(obj.getLong(END_DATE));
		}
		return dto;
	}

}

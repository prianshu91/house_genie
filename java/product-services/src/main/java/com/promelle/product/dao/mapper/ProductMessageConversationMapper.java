package com.promelle.product.dao.mapper;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.ProductMessageConversation;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductMessageConversationMapper implements
		MongoMapper<ProductMessageConversation, SearchFilter> {
	public static final String SENDER_ID = "senderId";
	public static final String RECEIVER_ID = "receiverId";
	public static final String PRODUCT_ID = "productId";

	@Override
	public BasicDBObject convertToDao(ProductMessageConversation dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, SENDER_ID, dto.getSenderId());
		MongoUtils.appendToObj(obj, RECEIVER_ID, dto.getReceiverId());
		MongoUtils.appendToObj(obj, PRODUCT_ID, dto.getProductId());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(SearchFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		return obj;
	}

	@Override
	public ProductMessageConversation convertToDto(BasicDBObject obj) {
		ProductMessageConversation dto = new ProductMessageConversation();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setSenderId(obj.getString(SENDER_ID));
		dto.setReceiverId(obj.getString(RECEIVER_ID));
		dto.setProductId(obj.getString(PRODUCT_ID));
		return dto;
	}

}

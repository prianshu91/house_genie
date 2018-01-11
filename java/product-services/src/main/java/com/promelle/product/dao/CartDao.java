package com.promelle.product.dao;

import com.mongodb.BasicDBObject;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.CartItemMapper;
import com.promelle.product.dto.CartItem;
import com.promelle.product.filter.CartItemFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class CartDao extends
		AbstractMongoDao<CartItem, CartItemFilter, CartItemMapper> {

	public CartDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "cart", CartItemMapper.class);
	}

	public int delete(CartItemFilter filter) {
		return getCollection().remove(getMapper().convertToDao(filter)).getN();
	}

	public int deleteById(String id) {
		return getCollection().remove(new BasicDBObject(MONGO_ID, id)).getN();
	}

}

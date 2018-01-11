package com.promelle.product.dao;

import com.mongodb.BasicDBObject;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.FavoriteMapper;
import com.promelle.product.dto.Favorite;
import com.promelle.product.filter.FavoriteFilter;

/**
 * This interface is intended for providing interactions with favourites table.
 * 
 * @author Kanak Sony
 * @version 1.0
 */
public class FavoriteDao extends AbstractMongoDao<Favorite, FavoriteFilter, FavoriteMapper> {

	public FavoriteDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "favourites", FavoriteMapper.class);
	}

	public int deleteById(String id) {
		return getCollection().remove(new BasicDBObject(MONGO_ID, id)).getN();
	}
}

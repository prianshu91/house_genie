package com.promelle.communication.dao;

import com.promelle.communication.dao.mapper.ProductMapper;
import com.promelle.communication.dto.Product;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductDao extends
		AbstractMongoDao<Product, SearchFilter, ProductMapper> {

	public ProductDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "products", ProductMapper.class);
	}

}

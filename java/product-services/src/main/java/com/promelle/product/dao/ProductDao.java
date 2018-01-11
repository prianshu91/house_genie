package com.promelle.product.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.ProductMapper;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.ProductFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductDao extends
		AbstractMongoDao<Product, ProductFilter, ProductMapper> {

	public ProductDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "products", ProductMapper.class);
	}

	public void updateImage(String id, String tag, String imageUrl)
			throws AbstractException {
		getCollection().update(
				new BasicDBObject(AbstractMongoDao.MONGO_ID, id),
				new BasicDBObject(AbstractMongoDao.SET_OPERATOR,
						new BasicDBObject(ProductMapper.IMAGES + "."
								+ tag.toLowerCase(), imageUrl).append(
								AbstractAuditDTO.MODIFIED_ON,
								new Date().getTime())));
	}

	public void addRentalPeriod(String id, Long startDate, Long endDate)
			throws AbstractException {
		getCollection().update(
				new BasicDBObject(AbstractMongoDao.MONGO_ID, id),
				new BasicDBObject(AbstractMongoDao.PUSH_OPERATOR,
						new BasicDBObject(ProductMapper.RENTED_ON,
								new BasicDBObject(ProductMapper.START,
										startDate).append(ProductMapper.END,
										endDate))));
	}

	/**
	 * Unblock rentedOn dates
	 * @param id
	 * @param list
	 * @throws AbstractException
	 */
	public void updateRentalPeriod(String id, List<Map<String, Long>> rentedOnList)
			throws AbstractException {
		getCollection().update(
				new BasicDBObject(AbstractMongoDao.MONGO_ID, id),
				new BasicDBObject(AbstractMongoDao.SET_OPERATOR,
						new BasicDBObject(ProductMapper.RENTED_ON, rentedOnList)));
	}

}

package com.promelle.product.dao;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.OrderMapper;
import com.promelle.product.dto.Order;
import com.promelle.product.filter.OrderFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderDao extends
		AbstractMongoDao<Order, OrderFilter, OrderMapper> {

	public OrderDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "orders", OrderMapper.class);
	}

}

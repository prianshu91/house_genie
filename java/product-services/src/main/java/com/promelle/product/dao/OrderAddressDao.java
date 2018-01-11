package com.promelle.product.dao;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.OrderAddressMapper;
import com.promelle.product.dto.OrderAddress;
import com.promelle.product.filter.OrderAddressFilter;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderAddressDao extends
		AbstractMongoDao<OrderAddress, OrderAddressFilter, OrderAddressMapper> {

	public OrderAddressDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "orderAddress", OrderAddressMapper.class);
	}

	public OrderAddress findByOrderId(AbstractRequestTracker requestTracker,
			String orderId) throws AbstractException {
		OrderAddressFilter addressFilter = new OrderAddressFilter();
		addressFilter.setOrderId(orderId);
		return findOne(addressFilter);
	}
	
}

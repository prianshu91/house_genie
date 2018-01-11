package com.promelle.product.dao;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.OrderItemMapper;
import com.promelle.product.dto.OrderItem;
import com.promelle.product.filter.OrderItemFilter;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class OrderItemDao extends
		AbstractMongoDao<OrderItem, OrderItemFilter, OrderItemMapper> {

	public OrderItemDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "orderItems", OrderItemMapper.class);
	}

	public List<OrderItem> findByOrderId(AbstractRequestTracker requestTracker,
			String orderId) throws AbstractException {
		OrderItemFilter filter = new OrderItemFilter();
		filter.setOrderId(orderId);
		return list(filter, null, null).getObjects();
	}

	public boolean updateShipment(AbstractRequestTracker requestTracker,
			String orderItemId, String shipmentId) throws AbstractException {
		OrderItemFilter filter = new OrderItemFilter();
		filter.setId(orderItemId);
		OrderItem item = new OrderItem();
		item.setShipmentId(shipmentId);
		return update(requestTracker, filter, item) > 0;
	}

	public List<OrderItem> findByShipmentId(AbstractRequestTracker requestTracker,
			String shipmentId) throws AbstractException {
		OrderItemFilter filter = new OrderItemFilter();
		filter.setShipmentId(shipmentId);
		return list(filter, null, null).getObjects();
	}

}

package com.promelle.product.dao;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.ShipmentMapper;
import com.promelle.product.dto.Shipment;
import com.promelle.product.filter.ShipmentFilter;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This interface is intended for providing interactions with shipment table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ShipmentDao extends
		AbstractMongoDao<Shipment, ShipmentFilter, ShipmentMapper> {

	public ShipmentDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "shipments", ShipmentMapper.class);
	}

	public List<Shipment> findByOrderId(AbstractRequestTracker requestTracker,
			String orderId) throws AbstractException {
		ShipmentFilter filter = new ShipmentFilter();
		filter.setOrderId(orderId);
		return list(filter, null, null).getObjects();
	}

	public String getShipmentStatus(AbstractRequestTracker requestTracker,
			String shipmentId) throws AbstractException {
		DBObject obj = getCollection().findOne(
				new BasicDBObject(MONGO_ID, shipmentId),
				new BasicDBObject(ShipmentMapper.SHIPMENT_STATUS, 1));
		return obj != null ? (String) obj.get(ShipmentMapper.SHIPMENT_STATUS)
				: null;
	}

}

package com.promelle.product.dao;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.EarningMapper;
import com.promelle.product.dto.Earning;
import com.promelle.product.filter.EarningFilter;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This interface is intended for providing interactions with shipment table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class EarningDao extends
		AbstractMongoDao<Earning, EarningFilter, EarningMapper> {

	public EarningDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "earnings", EarningMapper.class);
	}

	public Earning findByShipmentId(AbstractRequestTracker requestTracker,
			String shipmentId) throws AbstractException {
		EarningFilter filter = new EarningFilter();
		filter.setShipmentId(shipmentId);
		return findOne(filter);
	}

}

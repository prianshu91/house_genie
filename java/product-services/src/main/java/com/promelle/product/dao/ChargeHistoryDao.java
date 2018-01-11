package com.promelle.product.dao;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.ChargeHistoryMapper;
import com.promelle.product.dto.ChargeHistory;
import com.promelle.product.filter.ChargeHistoryFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ChargeHistoryDao extends
		AbstractMongoDao<ChargeHistory, ChargeHistoryFilter, ChargeHistoryMapper> {

	public ChargeHistoryDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "chargeHistory", ChargeHistoryMapper.class);
	}

}

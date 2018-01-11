package com.promelle.product.dao.mapper;

import java.sql.ResultSet;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.ChargeHistory;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.ChargeHistoryFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ChargeHistoryMapper implements
		MongoMapper<ChargeHistory, ChargeHistoryFilter> {
	public static final String ORDER_ID = "orderId";
	public static final String CHARGE = "charge";
	public static final String CHARGE_STR = "chargeStr";

	@Override
	public BasicDBObject convertToDao(ChargeHistory dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, CHARGE, dto.getCharge());
		MongoUtils.appendToObj(obj, CHARGE_STR, dto.getChargeStr());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(ChargeHistoryFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, ORDER_ID, filter.getOrderId());
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChargeHistory convertToDto(BasicDBObject obj) {
		ChargeHistory dto = new ChargeHistory();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setOrderId(obj.getString(ORDER_ID));
		dto.setChargeStr(obj.getString(CHARGE_STR));
		if (obj.containsField(CHARGE)) {
			dto.setCharge((Map<String, Object>) obj.get(CHARGE));
		}
		return dto;
	}

}

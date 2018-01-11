package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.promelle.constants.Punctuation;
import com.promelle.exception.TransformationException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.Shipment;
import com.promelle.product.dto.User;
import com.promelle.product.filter.ShipmentFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ShipmentMapper implements MongoMapper<Shipment, ShipmentFilter> {
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String OWNER_ID = "ownerId";
	public static final String ORDER_ID = "orderId";
	public static final String OWNER_NAME = "ownerName";
	public static final String SHIPMENT_STATUS = "shipmentStatus";
	public static final String TRACK = "track";
	public static final String USER = "user";
	public static final String OWNER = "owner";
	public static final String PRODUCT = "product";
	public static final String DELIVERY_CHARGE = "deliveryCharge";
	public static final String INSURANCE_AMOUNT = "insuranceAmount";

	@Override
	public BasicDBObject convertToDao(Shipment dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, USER_NAME, dto.getUserName());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, OWNER_ID, dto.getOwnerId());
		MongoUtils.appendToObj(obj, OWNER_NAME, dto.getOwnerName());
		MongoUtils.appendToObj(obj, SHIPMENT_STATUS, dto.getShipmentStatus());
		if (dto.getTrack() != null && !dto.getTrack().isEmpty()) {
			for (Entry<String, Long> entry : dto.getTrack().entrySet()) {
				obj.put(TRACK + Punctuation.DOT + entry.getKey(),
						entry.getValue());
			}
		}
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());

		try {
			if (dto.getUser() != null) {
				Map<String, Object> userMap = dto.getUser().toMap();
				userMap.replace("homeAddress", dto.getUser().getHomeAddress()
						.toMap());

				MongoUtils.appendToObj(obj, USER, new BasicDBObject(userMap));
			}

			if (dto.getOwner() != null) {

				Map<String, Object> ownerMap = dto.getOwner().toMap();
				ownerMap.replace("homeAddress", dto.getOwner().getHomeAddress()
						.toMap());

				MongoUtils.appendToObj(obj, OWNER, new BasicDBObject(ownerMap));
			}

			if (dto.getProduct() != null) {
				Map<String, Object> productMap = dto.getProduct().toMap();
				productMap.replace("dressLocation", (dto.getProduct()
						.getDressLocation() != null ? dto.getProduct()
						.getDressLocation().toMap() : ""));

				MongoUtils.appendToObj(obj, PRODUCT, new BasicDBObject(
						productMap));
			}

		} catch (TransformationException e) {
			e.printStackTrace();
		}
		MongoUtils.appendToObj(obj, DELIVERY_CHARGE, dto.getDeliveryCharge());
		MongoUtils.appendToObj(obj, INSURANCE_AMOUNT, dto.getInsuranceAmount());

		return obj;
	}

	@Override
	public BasicDBObject convertToDao(ShipmentFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		MongoUtils.appendToObj(obj, ORDER_ID, filter.getOrderId());
		MongoUtils.appendToObj(obj, OWNER_ID, filter.getOwnerId());
		if (filter.getShipmentStatus() != null) {
			obj.put(SHIPMENT_STATUS, filter.getShipmentStatus());
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Shipment convertToDto(BasicDBObject obj) {
		Shipment dto = new Shipment();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setUserName(obj.getString(USER_NAME));
		dto.setOrderId(obj.getString(ORDER_ID));
		dto.setOwnerId(obj.getString(OWNER_ID));
		dto.setOwnerName(obj.getString(OWNER_NAME));
		dto.setShipmentStatus(obj.getString(SHIPMENT_STATUS));
		if (obj.containsField(TRACK)) {
			dto.setTrack((Map<String, Long>) obj.get(TRACK));
		}
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		if (obj.containsField(MODIFIED_ON)) {
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		dto.setModifiedBy(obj.getString(MODIFIED_BY));
		if (obj.containsField(STATUS)) {
			dto.setStatus(obj.getInt(STATUS));
		}

		if (obj.containsField(USER)) {
			try {
				dto.setUser(new ObjectMapper().readValue(obj.get(USER)
						.toString(), User.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (obj.containsField(OWNER)) {
			try {
				dto.setOwner(new ObjectMapper().readValue(obj.get(OWNER)
						.toString(), User.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (obj.containsField(PRODUCT)) {
			try {
				dto.setProduct(new ObjectMapper().readValue(obj.get(PRODUCT)
						.toString(), Product.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (obj.containsField(DELIVERY_CHARGE)) {
			dto.setDeliveryCharge(obj.getDouble(DELIVERY_CHARGE));
		} else {
			dto.setDeliveryCharge(0.00);
		}

		if (obj.containsField(INSURANCE_AMOUNT)) {
			dto.setInsuranceAmount(obj.getDouble(INSURANCE_AMOUNT));
		} else {
			dto.setInsuranceAmount(0.00);
		}

		return dto;
	}

}

package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.promelle.exception.TransformationException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.ChequeAddress;
import com.promelle.product.dto.Earning;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.EarningFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class EarningMapper implements MongoMapper<Earning, EarningFilter> {
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String SHIPMENT_ID = "shipmentId";
	public static final String ORDER_ID = "orderId";
	public static final String OWNER_ID = "ownerId";
	public static final String OWNER_NAME = "ownerName";
	public static final String REDEEM_STATUS = "redeemStatus";
	public static final String AMOUNT = "amount";
	public static final String PERCENTAGE = "percentage";
	public static final String REDEEM_ON = "redeemOn";
	public static final String REDEEMED_ON = "redeemedOn";
	public static final String CHEQUE_NO = "chequeNo";
	public static final String CHEQUE_IMAGE = "chequeImage";
	public static final String PAYMENT_MODE = "paymentMode";
	public static final String CHEQUE_RECIPIENT = "chequeRecipient";
	public static final String CHEQUE_ADDRESS = "chequeAddress";
	public static final String RENTAL_PRICE = "rentalPrice";
	public static final String DELIVERY_CHARGE = "deliveryCharge";
	public static final String REDEEMABLE_AMOUNT = "redeemableAmount";
	public static final String CHEQUE_DATE = "chequeDate";
	public static final String NOTES = "notes";

	@Override
	public BasicDBObject convertToDao(Earning dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, USER_NAME, dto.getUserName());
		MongoUtils.appendToObj(obj, SHIPMENT_ID, dto.getShipmentId());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, OWNER_ID, dto.getOwnerId());
		MongoUtils.appendToObj(obj, OWNER_NAME, dto.getOwnerName());
		MongoUtils.appendToObj(obj, REDEEM_STATUS, dto.getRedeemStatus());
		MongoUtils.appendToObj(obj, REDEEM_ON, dto.getRedeemOn());
		MongoUtils.appendToObj(obj, REDEEMED_ON, dto.getRedeemedOn());
		MongoUtils.appendToObj(obj, AMOUNT, dto.getAmount());
		MongoUtils.appendToObj(obj, PERCENTAGE, dto.getPercentage());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		MongoUtils.appendToObj(obj, CHEQUE_NO, dto.getChequeNo());
		MongoUtils.appendToObj(obj, CHEQUE_IMAGE, dto.getChequeImage());
		MongoUtils.appendToObj(obj, PAYMENT_MODE, dto.getPaymentMode());
		MongoUtils.appendToObj(obj, CHEQUE_RECIPIENT, dto.getChequeRecipient());
		try {
			if (dto.getChequeAddress() != null) {
				MongoUtils.appendToObj(obj, CHEQUE_ADDRESS, new BasicDBObject(
						dto.getChequeAddress().toMap()));
			}
		} catch (TransformationException e) {
			e.printStackTrace();
		}
		MongoUtils.appendToObj(obj, RENTAL_PRICE, dto.getRentalPrice());
		MongoUtils.appendToObj(obj, DELIVERY_CHARGE, dto.getDeliveryCharge());
		MongoUtils.appendToObj(obj, REDEEMABLE_AMOUNT,
				dto.getRedeemableAmount());
		MongoUtils.appendToObj(obj, CHEQUE_DATE, dto.getChequeDate());
		MongoUtils.appendToObj(obj, NOTES, dto.getNotes());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(EarningFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, OWNER_ID, filter.getOwnerId());
		MongoUtils.appendToObj(obj, REDEEM_STATUS, filter.getRedeemStatus());
		MongoUtils.appendToObj(obj, SHIPMENT_ID, filter.getShipmentId());

		// PEA-635 for implementing search bar on payment options tab
		if (StringUtils.isNotBlank(filter.getText())) {
			BasicDBList list = new BasicDBList();
			BasicDBObject textFilter = new BasicDBObject("$regex",
					filter.getText()).append("$options", "i");
			list.add(new BasicDBObject(OWNER_NAME, textFilter));
			list.add(new BasicDBObject(ORDER_ID, textFilter));
			obj.put("$or", list);
		}
		return obj;
	}

	@Override
	public Earning convertToDto(BasicDBObject obj) {
		Earning dto = new Earning();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setUserName(obj.getString(USER_NAME));
		dto.setShipmentId(obj.getString(SHIPMENT_ID));
		dto.setOrderId(obj.getString(ORDER_ID));
		dto.setOwnerId(obj.getString(OWNER_ID));
		dto.setOwnerName(obj.getString(OWNER_NAME));
		dto.setRedeemStatus(obj.getString(REDEEM_STATUS));
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		if (obj.containsField(REDEEM_ON)) {
			dto.setRedeemOn(obj.getLong(REDEEM_ON));
		}
		if (obj.containsField(REDEEMED_ON)) {
			dto.setRedeemedOn(obj.getLong(REDEEMED_ON));
		}
		if (obj.containsField(AMOUNT)) {
			dto.setAmount(obj.getDouble(AMOUNT));
		}
		if (obj.containsField(PERCENTAGE)) {
			dto.setPercentage(obj.getDouble(PERCENTAGE));
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
		dto.setChequeNo(obj.getString(CHEQUE_NO));
		dto.setChequeImage(obj.getString(CHEQUE_IMAGE));
		dto.setPaymentMode(obj.getString(PAYMENT_MODE));
		dto.setChequeRecipient(obj.getString(CHEQUE_RECIPIENT));
		if (obj.containsField(CHEQUE_ADDRESS)) {
			try {
				dto.setChequeAddress(new ObjectMapper()
						.readValue(obj.get(CHEQUE_ADDRESS).toString(),
								ChequeAddress.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (obj.containsField(RENTAL_PRICE)) {
			dto.setRentalPrice(obj.getDouble(RENTAL_PRICE));
		} else {
			if (obj.containsField(AMOUNT)) {
				Double rentalPrice = Double.valueOf(new DecimalFormat(".##")
						.format(((obj.getDouble(AMOUNT) / 75) * 100)));
				dto.setRentalPrice(rentalPrice);
			}
		}

		if (obj.containsField(DELIVERY_CHARGE)) {
			dto.setDeliveryCharge(obj.getDouble(DELIVERY_CHARGE));
		} else {
			dto.setDeliveryCharge(0.00);
		}

		if (obj.containsField(REDEEMABLE_AMOUNT)) {
			dto.setRedeemableAmount(obj.getDouble(REDEEMABLE_AMOUNT));
		} else {
			if (obj.containsField(AMOUNT)) {
				dto.setRedeemableAmount(obj.getDouble(AMOUNT));
			}
		}
		if (obj.containsField(CHEQUE_DATE)) {
			dto.setCreatedOn(obj.getLong(CHEQUE_DATE));
		}

		if (obj.containsField(NOTES)) {
			dto.setNotes(obj.getString(NOTES));
		}

		return dto;
	}
}

package com.promelle.product.dao.mapper;

import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.Invoice;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.InvoiceFilter;

import static com.promelle.dto.AbstractAuditDTO.STATUS;
import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class InvoiceMapper implements MongoMapper<Invoice, InvoiceFilter> {
	public static final String USER_ID = "userId";
	public static final String ORDER_ID = "orderId";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String PAYMENT_MODE = "paymentMode";
	public static final String AMOUNT = "amount";
	public static final String PAYMENT_DATE = "paymentDate";
	
	@Override
	public BasicDBObject convertToDao(Invoice dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, USER_ID, dto.getUserId());
		MongoUtils.appendToObj(obj, ORDER_ID, dto.getOrderId());
		MongoUtils.appendToObj(obj, TRANSACTION_ID, dto.getTransactionId());
		MongoUtils.appendToObj(obj, PAYMENT_MODE, dto.getPaymentMode());
		MongoUtils.appendToObj(obj, AMOUNT, dto.getAmount());
		MongoUtils.appendToObj(obj, PAYMENT_DATE, dto.getPaymentDate());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(InvoiceFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, USER_ID, filter.getUserId());
		MongoUtils.appendToObj(obj, ORDER_ID, filter.getOrderId());
		return obj;
	}

	@Override
	public Invoice convertToDto(BasicDBObject obj) {
		Invoice dto = new Invoice();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUserId(obj.getString(USER_ID));
		dto.setOrderId(obj.getString(ORDER_ID));
		dto.setTransactionId(obj.getString(TRANSACTION_ID));
		dto.setPaymentMode(obj.getString(PAYMENT_MODE));
		if (obj.containsField(AMOUNT)) {
			dto.setAmount(obj.getDouble(AMOUNT));
		}
		if (obj.containsField(PAYMENT_DATE)) {
			dto.setPaymentDate(obj.getLong(PAYMENT_DATE));
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
		return dto;
	}

}

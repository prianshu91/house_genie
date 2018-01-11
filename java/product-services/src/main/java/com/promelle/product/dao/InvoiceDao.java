package com.promelle.product.dao;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.InvoiceMapper;
import com.promelle.product.dto.Invoice;
import com.promelle.product.filter.InvoiceFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class InvoiceDao extends
		AbstractMongoDao<Invoice, InvoiceFilter, InvoiceMapper> {

	public InvoiceDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "invoices", InvoiceMapper.class);
	}

}

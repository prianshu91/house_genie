package com.promelle.product.dao;

import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.product.dao.mapper.PaymentsMapper;
import com.promelle.product.dto.Payments;
import com.promelle.product.filter.PaymentsFilter;

/**
 * This interface is intended for providing interactions with payments table.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
@RegisterMapper(PaymentsMapper.class)
@UseStringTemplate3StatementLocator
public abstract class PaymentsDao extends
		AbstractMariaDao<Payments, PaymentsFilter, PaymentsMapper> {

	public PaymentsDao() throws AbstractException {
		super(PaymentsMapper.class);
	}
}

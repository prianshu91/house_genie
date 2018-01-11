package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.product.dto.Payments;
import com.promelle.product.filter.PaymentsFilter;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Payments} .
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class PaymentsMapper implements MariaMapper<Payments, PaymentsFilter> {

	private static final String ID = "id";
	private static final String STRIPE_CUSTOMER_ID = "stripeCustomerId";
	private static final String USER_ID = "userId";

	@Override
	public Payments map(int index, ResultSet rs, StatementContext sc)
			throws SQLException {
		Payments obj = new Payments();
		obj.setId(rs.getString(ID));
		obj.setStripeCustomerId(rs.getString(STRIPE_CUSTOMER_ID));
		obj.setUserId(rs.getString(USER_ID));
		obj.setStatus(rs.getInt(STATUS));
		obj.setCreatedOn(rs.getLong(CREATED_ON));
		obj.setModifiedOn(rs.getLong(MODIFIED_ON));
		obj.setCreatedBy(rs.getString(CREATED_BY));
		obj.setModifiedBy(rs.getString(MODIFIED_BY));
		return obj;
	}

	@Override
	public Map<String, Object> convertToDao(Payments dto) {
		Map<String, Object> map = new HashMap<>();
		if (dto == null) {
			return map;
		}
		MapUtils.put(map, ID, dto.getId());
		MapUtils.put(map, STRIPE_CUSTOMER_ID, dto.getStripeCustomerId());
		MapUtils.put(map, USER_ID, dto.getUserId());
		MapUtils.put(map, STATUS, dto.getStatus());
		MapUtils.put(map, CREATED_ON, dto.getCreatedOn());
		MapUtils.put(map, MODIFIED_ON, dto.getModifiedOn());
		MapUtils.put(map, CREATED_BY, dto.getCreatedBy());
		MapUtils.put(map, MODIFIED_BY, dto.getModifiedBy());
		return map;
	}

	@Override
	public Map<String, Object> convertToDao(PaymentsFilter filter) {
		Map<String, Object> map = new HashMap<>();
		if (filter == null) {
			return map;
		}
		MapUtils.put(map, ID, filter.getId());
		MapUtils.put(map, USER_ID, filter.getUserId());
		MapUtils.put(map, STRIPE_CUSTOMER_ID, filter.getStripeCustomerId());
		MapUtils.put(map, STATUS, filter.getStatus());
		return map;
	}

	@Override
	public String getTableName() {
		return "payments";
	}

}

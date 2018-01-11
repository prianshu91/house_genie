package com.promelle.user.dao.mapper;

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
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.Account;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping account info to db.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class AccountMapper implements MariaMapper<Account, SearchFilter> {

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String PORTAL_NAME = "portalName";
	private static final String OWNER_ID = "ownerId";
	private static final String PARENT_ID = "parentId";

	@Override
	public Account map(int index, ResultSet rs, StatementContext sc)
			throws SQLException {
		Account obj = new Account();
		obj.setId(rs.getString(ID));
		obj.setName(rs.getString(NAME));
		obj.setPortalName(rs.getString(PORTAL_NAME));
		obj.setOwnerId(rs.getString(OWNER_ID));
		obj.setParentId(rs.getString(PARENT_ID));
		obj.setStatus(rs.getInt(STATUS));
		obj.setCreatedOn(rs.getLong(CREATED_ON));
		obj.setModifiedOn(rs.getLong(MODIFIED_ON));
		obj.setCreatedBy(rs.getString(CREATED_BY));
		obj.setModifiedBy(rs.getString(MODIFIED_BY));
		return obj;
	}

	@Override
	public Map<String, Object> convertToDao(Account dto) {
		Map<String, Object> map = new HashMap<>();
		if (dto == null) {
			return map;
		}
		MapUtils.put(map, ID, dto.getId());
		MapUtils.put(map, NAME, dto.getName());
		MapUtils.put(map, PORTAL_NAME, dto.getPortalName());
		MapUtils.put(map, OWNER_ID, dto.getOwnerId());
		MapUtils.put(map, PARENT_ID, dto.getId());
		MapUtils.put(map, STATUS, dto.getStatus());
		MapUtils.put(map, CREATED_ON, dto.getCreatedOn());
		MapUtils.put(map, MODIFIED_ON, dto.getModifiedOn());
		MapUtils.put(map, CREATED_BY, dto.getCreatedBy());
		MapUtils.put(map, MODIFIED_BY, dto.getModifiedBy());
		return map;
	}

	@Override
	public String getTableName() {
		return "account";
	}

}

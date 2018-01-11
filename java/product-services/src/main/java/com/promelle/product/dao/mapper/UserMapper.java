package com.promelle.product.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.skife.jdbi.v2.StatementContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.address.Address;
import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.product.dto.User;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link User}.
 * 
 * @author Kanak Sony
 * @version 1.0
 */
public class UserMapper implements MariaMapper<User, SearchFilter> {
	private static final String ID = "id";
	private static final String EMAIL = "email";
	private static final String MOBILE = "mobile";
	public static final String USERNAME = "username";
	private static final String ORGANIZATION_NAME = "organizationName";
	public static final String HOME_ADDRESS = "homeAddress";
	public static final String NAME = "name";
	public static final String SCHOOL_ID = "schoolId";

	@Override
	public User map(int index, ResultSet rs, StatementContext sc)
			throws SQLException {
		User obj = new User();
		obj.setId(rs.getString(ID));
		obj.setEmail(rs.getString(EMAIL));
		obj.setMobile(rs.getString(MOBILE));
		obj.setUsername(rs.getString(USERNAME));
		obj.setOrganizationName(rs.getString(ORGANIZATION_NAME));
		obj.setName(rs.getString(NAME));
		String homeAddress = rs.getString(HOME_ADDRESS);
		if (StringUtils.isNotBlank(homeAddress)) {
			try {
				obj.setHomeAddress(new ObjectMapper().readValue(homeAddress,
						Address.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (rs.getString(SCHOOL_ID) != null) {
			obj.setSchoolId(rs.getString(SCHOOL_ID));
		}
		return obj;
	}

	@Override
	public Map<String, Object> convertToDao(User dto) {
		Map<String, Object> map = new HashMap<>();
		if (dto == null) {
			return map;
		}
		MapUtils.put(map, ID, dto.getId());
		MapUtils.put(map, EMAIL, dto.getEmail());
		MapUtils.put(map, MOBILE, dto.getMobile());
		MapUtils.put(map, USERNAME, dto.getUsername());
		MapUtils.put(map, ORGANIZATION_NAME, dto.getOrganizationName());
		if (dto.getHomeAddress() != null) {
			MapUtils.put(map, HOME_ADDRESS, dto.getHomeAddress().toString());
		}
		MapUtils.put(map, NAME, dto.getName());
		if (dto.getSchoolId() != null) {
			MapUtils.put(map, SCHOOL_ID, dto.getSchoolId());
		}
		return map;
	}

	@Override
	public Map<String, Object> convertToDao(SearchFilter filter) {
		Map<String, Object> map = new HashMap<>();
		if (filter == null) {
			return map;
		}
		MapUtils.put(map, ID, filter.getId());
		return map;
	}

	@Override
	public String getTableName() {
		return "user";
	}

}

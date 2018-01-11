package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.skife.jdbi.v2.StatementContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.address.Address;
import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.user.dto.User;
import com.promelle.user.filter.UserFilter;
import com.promelle.utils.MapUtils;

import static com.promelle.dto.AbstractAuditDTO.STATUS;
import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link User}
 * .
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserMapper implements MariaMapper<User, UserFilter> {
	private static final String ID = "id";
	public static final String EMAIL = "email";
	private static final String ACCOUNT_ID = "accountId";
	private static final String PORTAL_NAME = "portalName";
	public static final String NAME = "name";
	public static final String MOBILE = "mobile";
	private static final String PICTURE = "picture";
	public static final String USERNAME = "username";
	public static final String ORGANIZATION_NAME = "organizationName";
	private static final String PASWD = "password";
	public static final String HOME_ADDRESS = "homeAddress";
	public static final String BIRTHDAY = "birthday";
	public static final String OTP = "otp";
	public static final String SCHOOL_ID = "schoolId";

	@Override
	public User map(int index, ResultSet rs, StatementContext sc)
			throws SQLException {
		User obj = new User();
		obj.setId(rs.getString(ID));
		obj.setEmail(rs.getString(EMAIL));
		obj.setAccountId(rs.getString(ACCOUNT_ID));
		obj.setPortalName(rs.getString(PORTAL_NAME));
		obj.setName(rs.getString(NAME));
		obj.setMobile(rs.getString(MOBILE));
		obj.setPicture(rs.getString(PICTURE));
		if (StringUtils.isBlank(obj.getPicture())) {
			obj.setPicture("images/image-upload.png");
		}
		obj.setUsername(rs.getString(USERNAME));
		obj.setOrganizationName(rs.getString(ORGANIZATION_NAME));
		obj.setStatus(rs.getInt(STATUS));
		obj.setPassword(rs.getString(PASWD));
		obj.setCreatedOn(rs.getLong(CREATED_ON));
		obj.setModifiedOn(rs.getLong(MODIFIED_ON));
		obj.setCreatedBy(rs.getString(CREATED_BY));
		obj.setModifiedBy(rs.getString(MODIFIED_BY));
		String homeAddress = rs.getString(HOME_ADDRESS);
		if (StringUtils.isNotBlank(homeAddress)) {
			try {
				obj.setHomeAddress(new ObjectMapper().readValue(homeAddress,
						Address.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		obj.setBirthday(rs.getLong(BIRTHDAY));
		obj.setOtp(rs.getString(OTP));
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
		MapUtils.put(map, ACCOUNT_ID, dto.getAccountId());
		MapUtils.put(map, PORTAL_NAME, dto.getPortalName());
		MapUtils.put(map, NAME, dto.getName());
		MapUtils.put(map, MOBILE, dto.getMobile());
		MapUtils.put(map, PICTURE, dto.getPicture());
		MapUtils.put(map, USERNAME, dto.getUsername());
		MapUtils.put(map, ORGANIZATION_NAME, dto.getOrganizationName());
		MapUtils.put(map, STATUS, dto.getStatus());
		MapUtils.put(map, PASWD, dto.getPassword());
		MapUtils.put(map, CREATED_ON, dto.getCreatedOn());
		MapUtils.put(map, MODIFIED_ON, dto.getModifiedOn());
		MapUtils.put(map, CREATED_BY, dto.getCreatedBy());
		MapUtils.put(map, MODIFIED_BY, dto.getModifiedBy());
		if (dto.getHomeAddress() != null) {
			MapUtils.put(map, HOME_ADDRESS, dto.getHomeAddress().toString());
		}
		MapUtils.put(map, BIRTHDAY, dto.getBirthday());
		MapUtils.put(map, OTP, dto.getOtp());
		if (dto.getSchoolId() != null) {
			MapUtils.put(map, SCHOOL_ID, dto.getSchoolId());
		}
		return map;
	}

	@Override
	public Map<String, Object> convertToDao(UserFilter filter) {
		Map<String, Object> map = new HashMap<>();
		if (filter == null) {
			return map;
		}
		MapUtils.put(map, ID, filter.getId());
		MapUtils.put(map, ACCOUNT_ID, filter.getAccountId());
		MapUtils.put(map, PORTAL_NAME, filter.getPortalName());
		MapUtils.put(map, USERNAME, filter.getUsername());
		MapUtils.put(map, EMAIL, filter.getEmail());
		MapUtils.put(map, MOBILE, filter.getMobile());
		MapUtils.put(map, STATUS, filter.getStatus());
		MapUtils.put(map, SCHOOL_ID, filter.getSchoolId());
		return map;
	}

	@Override
	public String getTableName() {
		return "user";
	}

	@Override
	public Map<String, String> getSortMap() {
		Map<String, String> sortMap = new HashMap<String, String>();
		sortMap.put(CREATED_ON, CREATED_ON);
		sortMap.put(MODIFIED_ON, MODIFIED_ON);
		return sortMap;
	}

}

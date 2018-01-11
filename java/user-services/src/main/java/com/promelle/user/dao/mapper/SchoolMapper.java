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

import org.apache.commons.lang.StringUtils;
import org.skife.jdbi.v2.StatementContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.address.Address;
import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.user.dto.School;
import com.promelle.user.filter.SchoolFilter;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link School} .
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class SchoolMapper implements MariaMapper<School, SchoolFilter> {

	private static final String ID = "id";
	public static final String SCHOOL_NAME = "schoolName";
	private static final String SCHOOL_ADDRESS = "schoolAddress";
	private static final String SCHOOL_POD_NAME = "schoolPodName";
	private static final String SCHOOL_POD_LOGO = "schoolPodLogo";

	@Override
	public School map(int index, ResultSet rs, StatementContext sc)
			throws SQLException {
		School obj = new School();
		obj.setId(rs.getString(ID));
		obj.setSchoolName(rs.getString(SCHOOL_NAME));

		String schoolAddress = rs.getString(SCHOOL_ADDRESS);
		if (StringUtils.isNotBlank(schoolAddress)) {
			try {
				obj.setSchoolAddress(new ObjectMapper().readValue(
						schoolAddress, Address.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		obj.setSchoolPodName(rs.getString(SCHOOL_POD_NAME));
		if (StringUtils.isNotBlank(rs.getString(SCHOOL_POD_LOGO)))
			obj.setSchoolPodLogo(rs.getString(SCHOOL_POD_LOGO));

		obj.setStatus(rs.getInt(STATUS));
		obj.setCreatedOn(rs.getLong(CREATED_ON));
		obj.setModifiedOn(rs.getLong(MODIFIED_ON));
		obj.setCreatedBy(rs.getString(CREATED_BY));
		obj.setModifiedBy(rs.getString(MODIFIED_BY));
		return obj;
	}

	@Override
	public Map<String, Object> convertToDao(School dto) {
		Map<String, Object> map = new HashMap<>();
		if (dto == null) {
			return map;
		}
		MapUtils.put(map, ID, dto.getId());
		MapUtils.put(map, SCHOOL_NAME, dto.getSchoolName());
		if (dto.getSchoolAddress() != null) {
			MapUtils.put(map, SCHOOL_ADDRESS, dto.getSchoolAddress().toString());
		}

		MapUtils.put(map, SCHOOL_POD_NAME, dto.getSchoolPodName());
		if (dto.getSchoolPodLogo() != null)
			MapUtils.put(map, SCHOOL_POD_LOGO, dto.getSchoolPodLogo());

		MapUtils.put(map, STATUS, dto.getStatus());
		MapUtils.put(map, CREATED_ON, dto.getCreatedOn());
		MapUtils.put(map, MODIFIED_ON, dto.getModifiedOn());
		MapUtils.put(map, CREATED_BY, dto.getCreatedBy());
		MapUtils.put(map, MODIFIED_BY, dto.getModifiedBy());
		return map;
	}

	@Override
	public Map<String, Object> convertToDao(SchoolFilter filter) {
		Map<String, Object> map = new HashMap<>();
		if (filter == null) {
			return map;
		}
		MapUtils.put(map, ID, filter.getId());
		MapUtils.put(map, SCHOOL_NAME, filter.getSchoolName());
		MapUtils.put(map, STATUS, filter.getStatus());
		MapUtils.put(map, SCHOOL_POD_NAME, filter.getSchoolPodName());
		return map;
	}

	@Override
	public String getTableName() {
		return "schools";
	}

	@Override
	public Map<String, String> getSortMap() {
		Map<String, String> sortMap = new HashMap<String, String>();
		sortMap.put(CREATED_ON, CREATED_ON);
		sortMap.put(MODIFIED_ON, MODIFIED_ON);
		sortMap.put(SCHOOL_NAME, SCHOOL_NAME);
		return sortMap;
	}

}

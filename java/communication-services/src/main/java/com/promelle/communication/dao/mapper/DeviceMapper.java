package com.promelle.communication.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.communication.dto.Device;
import com.promelle.communication.dto.User;
import com.promelle.communication.filter.DeviceFilter;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link User}
 * .
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DeviceMapper implements MariaMapper<Device, DeviceFilter> {

	private static final String ID = "id";
	private static final String DEVICE_ID = "deviceId";
	private static final String DEVICE_TYPE = "deviceType";
	private static final String DEVICE_TOKEN = "deviceToken";
	private static final String USER_ID = "userId";
	private static final String STATUS = "status";

	@Override
	public Device map(int index, ResultSet rs, StatementContext sc)
			throws SQLException {
		Device obj = new Device();
		obj.setId(rs.getString(ID));
		obj.setDeviceId(rs.getString(DEVICE_ID));
		obj.setDeviceType(rs.getString(DEVICE_TYPE));
		obj.setDeviceToken(rs.getString(DEVICE_TOKEN));
		obj.setUserId(rs.getString(USER_ID));
		return obj;
	}

	@Override
	public Map<String, Object> convertToDao(Device dto) {
		Map<String, Object> map = new HashMap<>();
		if (dto == null) {
			return map;
		}
		MapUtils.put(map, ID, dto.getId());
		MapUtils.put(map, DEVICE_ID, dto.getDeviceId());
		MapUtils.put(map, DEVICE_TYPE, dto.getDeviceType());
		MapUtils.put(map, DEVICE_TOKEN, dto.getDeviceToken());
		MapUtils.put(map, USER_ID, dto.getUserId());
		return map;
	}

	@Override
	public Map<String, Object> convertToDao(DeviceFilter filter) {
		Map<String, Object> map = new HashMap<>();
		if (filter == null) {
			return map;
		}
		MapUtils.put(map, ID, filter.getId());
		MapUtils.put(map, USER_ID, filter.getUserId());
		MapUtils.put(map, DEVICE_ID, filter.getDeviceId());
		MapUtils.put(map, DEVICE_TOKEN, filter.getDeviceToken());
		MapUtils.put(map, STATUS, filter.getStatus());
		return map;
	}

	@Override
	public String getTableName() {
		return "devices";
	}

}

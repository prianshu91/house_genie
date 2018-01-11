package com.promelle.communication.dao;

import java.util.ArrayList;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.communication.dao.mapper.DeviceMapper;
import com.promelle.communication.dto.Device;
import com.promelle.communication.filter.DeviceFilter;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This interface is intended for providing interactions with devices table.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
@RegisterMapper(DeviceMapper.class)
@UseStringTemplate3StatementLocator
public abstract class DeviceDao extends
		AbstractMariaDao<Device, DeviceFilter, DeviceMapper> {

	public DeviceDao() throws AbstractException {
		super(DeviceMapper.class);
	}

	public List<String> getDevices(AbstractRequestTracker requestTracker,
			DeviceFilter filter) {

		List<Device> devices = list(requestTracker, filter).getObjects();
		List<String> deviceList = null;
		
		if (devices != null && devices.size() > 0) {
			deviceList = new ArrayList<String>();
			for (Device device : devices) {
				deviceList.add(device.getDeviceToken());
			}
		}

		return deviceList;
	}
}

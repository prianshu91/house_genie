package com.promelle.user.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.user.dao.mapper.DeviceMapper;
import com.promelle.user.dto.Device;
import com.promelle.user.filter.DeviceFilter;

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

	/**
	 * Update deviceToken.
	 * 
	 * @param id
	 * @param deviceTOken
	 * @return 1 for success & 0 for failure
	 */
	@SqlUpdate("update devices set deviceToken= :deviceToken where id= :id")
	public abstract int updateDeviceToken(@Bind("id") String id,
			@Bind("deviceToken") String deviceToken);

	/**
	 * Update device status.
	 *
	 * @param id
	 * @param status
	 * @return 1 for success & 0 for failure
	 */
	@SqlUpdate("update devices set status= :status where id= :id")
	public abstract int updateDeviceStatus(@Bind("id") String id,
			@Bind("status") Integer status);

}

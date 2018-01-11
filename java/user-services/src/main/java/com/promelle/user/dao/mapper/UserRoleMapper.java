package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.UserRole;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link UserRole}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserRoleMapper implements MariaMapper<UserRole, SearchFilter> {

	public static final String ID = "id";
	public static final String ROLE_ID = "roleId";
	public static final String USER_ID = "userId";

    @Override
    public UserRole map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        UserRole obj = new UserRole();
        obj.setId(rs.getString(ID));
        obj.setRoleId(rs.getString(ROLE_ID));
        obj.setUserId(rs.getString(USER_ID));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(UserRole dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, USER_ID, dto.getUserId());
        MapUtils.put(map, ROLE_ID, dto.getRoleId());
        return map;
    }

    @Override
    public String getTableName() {
        return "userRole";
    }

}

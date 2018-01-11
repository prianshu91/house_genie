package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.UserPermission;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link UserPermission}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserPermissionMapper implements MariaMapper<UserPermission, SearchFilter> {

    private static final String ID = "id";
    private static final String PERMISSION_ID = "permissionId";
    private static final String USER_ID = "userId";

    @Override
    public UserPermission map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        UserPermission obj = new UserPermission();
        obj.setId(rs.getString(ID));
        obj.setUserId(rs.getString(USER_ID));
        obj.setPermissionId(rs.getString(PERMISSION_ID));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(UserPermission dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, USER_ID, dto.getUserId());
        MapUtils.put(map, PERMISSION_ID, dto.getPermissionId());
        System.out.println("map => " + map);
        return map;
    }

    @Override
    public String getTableName() {
        return "userPermission";
    }

}

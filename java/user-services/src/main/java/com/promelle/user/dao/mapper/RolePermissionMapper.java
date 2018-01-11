package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.RolePermission;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link RolePermission}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class RolePermissionMapper implements MariaMapper<RolePermission, SearchFilter> {

    private static final String ID = "id";
    private static final String PERMISSION_ID = "permissionId";
    private static final String ROLE_ID = "roleId";

    @Override
    public RolePermission map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        RolePermission obj = new RolePermission();
        obj.setId(rs.getString(ID));
        obj.setRoleId(rs.getString(ROLE_ID));
        obj.setPermissionId(rs.getString(PERMISSION_ID));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(RolePermission dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, ROLE_ID, dto.getRoleId());
        MapUtils.put(map, PERMISSION_ID, dto.getPermissionId());
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
        return "rolePermission";
    }

}

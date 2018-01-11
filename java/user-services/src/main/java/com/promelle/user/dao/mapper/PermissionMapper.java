package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.Permission;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link Permission}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class PermissionMapper implements MariaMapper<Permission, SearchFilter> {

    public static final String TABLE = "`permission`";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";

    @Override
    public Permission map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        Permission obj = new Permission();
        obj.setId(rs.getString(ID));
        obj.setName(rs.getString(NAME));
        obj.setCode(rs.getString(CODE));
        obj.setDescription(rs.getString(DESCRIPTION));
        obj.setStatus(rs.getInt(STATUS));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(Permission dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, NAME, dto.getName());
        MapUtils.put(map, CODE, dto.getCode());
        MapUtils.put(map, DESCRIPTION, dto.getDescription());
        map.put(STATUS, dto.getStatus());
        return map;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

}

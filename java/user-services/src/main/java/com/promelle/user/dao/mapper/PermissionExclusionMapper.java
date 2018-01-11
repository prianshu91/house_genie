package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.PermissionExclusion;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link PermissionExclusion}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class PermissionExclusionMapper implements MariaMapper<PermissionExclusion, SearchFilter> {
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String PERMISSION_ID = "permissionId";
    private static final String REF_ID = "refId";

    @Override
    public PermissionExclusion map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        PermissionExclusion obj = new PermissionExclusion();
        obj.setId(rs.getString(ID));
        obj.setType(rs.getString(TYPE));
        obj.setPermissionId(rs.getString(PERMISSION_ID));
        obj.setRefId(rs.getString(REF_ID));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(PermissionExclusion dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, PERMISSION_ID, dto.getPermissionId());
        MapUtils.put(map, TYPE, dto.getType());
        MapUtils.put(map, REF_ID, dto.getRefId());
        return map;
    }

    @Override
    public String getTableName() {
        return "permissionExclusion";
    }

}

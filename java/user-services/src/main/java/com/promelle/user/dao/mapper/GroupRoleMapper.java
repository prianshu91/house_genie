package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.GroupRole;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link GroupRole}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class GroupRoleMapper implements MariaMapper<GroupRole, SearchFilter> {
    private static final String ID = "id";
    private static final String ROLE_ID = "roleId";
    private static final String GROUP_ID = "groupID";

    @Override
    public GroupRole map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        GroupRole obj = new GroupRole();
        obj.setId(rs.getString(ID));
        obj.setRoleId(rs.getString(ROLE_ID));
        obj.setGroupId(rs.getString(GROUP_ID));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(GroupRole dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, ROLE_ID, dto.getRoleId());
        MapUtils.put(map, GROUP_ID, dto.getGroupId());
        return map;
    }

    @Override
    public String getTableName() {
        return "groupRole";
    }

}

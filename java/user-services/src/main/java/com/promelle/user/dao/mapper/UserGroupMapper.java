package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.UserGroup;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link UserGroup}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserGroupMapper implements MariaMapper<UserGroup, SearchFilter> {

    private static final String ID = "id";
    private static final String GROUP_ID = "groupId";
    private static final String USER_ID = "userId";

    @Override
    public UserGroup map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        UserGroup obj = new UserGroup();
        obj.setId(rs.getString(ID));
        obj.setUserId(rs.getString(USER_ID));
        obj.setGroupId(rs.getString(GROUP_ID));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(UserGroup dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, USER_ID, dto.getUserId());
        MapUtils.put(map, GROUP_ID, dto.getGroupId());
        return map;
    }

    @Override
    public String getTableName() {
        return "userGroup";
    }

}

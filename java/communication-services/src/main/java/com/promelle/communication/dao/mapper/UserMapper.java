package com.promelle.communication.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.communication.dto.User;
import com.promelle.filter.SearchFilter;
import com.promelle.utils.MapUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link User}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserMapper implements MariaMapper<User, SearchFilter> {
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String MOBILE = "mobile";
    private static final String USERNAME = "username";
    private static final String ORGANIZATION_NAME ="organizationName";

    @Override
    public User map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        User obj = new User();
        obj.setId(rs.getString(ID));
        obj.setEmail(rs.getString(EMAIL));
        obj.setName(rs.getString(NAME));
        obj.setMobile(rs.getString(MOBILE));
        obj.setUsername(rs.getString(USERNAME));
        obj.setOrganizationName(rs.getString(ORGANIZATION_NAME));
        return obj;
    }

    @Override
    public Map<String, Object> convertToDao(User dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, EMAIL, dto.getEmail());
        MapUtils.put(map, NAME, dto.getName());
        MapUtils.put(map, MOBILE, dto.getMobile());
        MapUtils.put(map, USERNAME, dto.getUsername());
        MapUtils.put(map, ORGANIZATION_NAME, dto.getOrganizationName());
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
        return "user";
    }

}

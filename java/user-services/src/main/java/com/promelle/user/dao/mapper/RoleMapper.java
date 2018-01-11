package com.promelle.user.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.skife.jdbi.v2.StatementContext;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dto.Role;
import com.promelle.utils.MapUtils;

import static com.promelle.dto.AbstractAuditDTO.STATUS;
import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;

/**
 * This class is intended for mapping a row of {@link ResultSet} to {@link Role}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class RoleMapper implements MariaMapper<Role, SearchFilter> {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";

    @Override
    public Role map(int index, ResultSet rs, StatementContext sc) throws SQLException {
        Role obj = new Role();
        obj.setId(rs.getString(ID));
        obj.setName(rs.getString(NAME));
        obj.setCode(rs.getString(CODE));
        obj.setDescription(rs.getString(DESCRIPTION));
        obj.setStatus(rs.getInt(STATUS));
        obj.setCreatedOn(rs.getLong(CREATED_ON));
        obj.setModifiedOn(rs.getLong(MODIFIED_ON));
        obj.setCreatedBy(rs.getString(CREATED_BY));
        obj.setModifiedBy(rs.getString(MODIFIED_BY));
        return obj;
    }

    @Override
    public String getTableName() {
        return "role";
    }

    @Override
    public Map<String, Object> convertToDao(Role dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            return map;
        }
        MapUtils.put(map, ID, dto.getId());
        MapUtils.put(map, NAME, dto.getName());
        MapUtils.put(map, CODE, dto.getCode());
        MapUtils.put(map, DESCRIPTION, dto.getDescription());
        MapUtils.put(map, STATUS, dto.getStatus());
        MapUtils.put(map, CREATED_ON, dto.getCreatedOn());
        MapUtils.put(map, MODIFIED_ON, dto.getModifiedOn());
        MapUtils.put(map, CREATED_BY, dto.getCreatedBy());
        MapUtils.put(map, MODIFIED_BY, dto.getModifiedBy());
        return map;
    }

}

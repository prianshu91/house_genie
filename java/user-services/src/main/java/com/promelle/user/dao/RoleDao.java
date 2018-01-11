package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.RoleMapper;
import com.promelle.user.dto.Role;

/**
 * This interface is intended for providing interactions with role table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(RoleMapper.class)
@UseStringTemplate3StatementLocator
public abstract class RoleDao extends AbstractMariaDao<Role, SearchFilter, RoleMapper> {

    public RoleDao() throws AbstractException {
        super(RoleMapper.class);
    }

    /**
     * Find multiple roles by multiple ids.
     * 
     * @param roleIds
     * @return list of matching roles.
     */
    @SqlQuery("select * from `role` where id in (<roleIds>) and status=1")
    public abstract List<Role> findByIds(@BindIn("roleIds") List<String> roleIds);

    /**
     * Find all active roles.
     * 
     * @return list of active roles.
     */
    @SqlQuery("select * from `role` where status=1")
    public abstract List<Role> findAll();

    /**
     * Find a role by code.
     * 
     * @param code
     * @return matching role.
     */
    @SqlQuery("select * from `role` where code= :code and status=1")
    public abstract Role findByCode(@Bind("code") String code);

    /**
     * Find all roles except the excluded role ids.
     * 
     * @param roleIds
     * @return list of roles.
     */
    @SqlQuery("select * from `role` where id not in (<roleIds>) and status=1")
    public abstract List<Role> findAllMinusIds(@BindIn("roleIds") List<String> roleIds);

}

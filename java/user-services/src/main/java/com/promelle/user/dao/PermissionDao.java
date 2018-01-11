package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.PermissionMapper;
import com.promelle.user.dto.Permission;

/**
 * This interface is intended for providing interactions with permission table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(PermissionMapper.class)
@UseStringTemplate3StatementLocator
public abstract class PermissionDao extends AbstractMariaDao<Permission, SearchFilter, PermissionMapper> {

    public PermissionDao() throws AbstractException {
        super(PermissionMapper.class);
    }

    /**
     * Find multiple permissions by multiple ids.
     * 
     * @param permissionIds
     * @return list of matching permissions.
     */
    @SqlQuery("select * from `permission` where id in (<permissionIds>) and status=1")
    public abstract List<Permission> findByIds(@BindIn("permissionIds") List<String> permissionIds);

    /**
     * Find all active permissions.
     * 
     * @return list of active permissions.
     */
    @SqlQuery("select * from `permission` where status=1")
    public abstract List<Permission> findAll();

    /**
     * Find all permissions except the excluded permission ids.
     * 
     * @param permissionIds
     * @return list of permissions.
     */
    @SqlQuery("select * from `permission` where id not in (<permissionIds>) and status=1")
    public abstract List<Permission> findAllMinusIds(@BindIn("permissionIds") List<String> permissionIds);

}

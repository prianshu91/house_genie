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
import com.promelle.user.dao.mapper.RolePermissionMapper;
import com.promelle.user.dto.RolePermission;

/**
 * This interface is intended for providing interactions with rolePermission table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(RolePermissionMapper.class)
@UseStringTemplate3StatementLocator
public abstract class RolePermissionDao extends AbstractMariaDao<RolePermission, SearchFilter, RolePermissionMapper> {

    public RolePermissionDao() throws AbstractException {
        super(RolePermissionMapper.class);
    }

    /**
     * Find all role permission associations for provided role id.
     * 
     * @param roleId
     * @return list of matching role permissions associations.
     */
    @SqlQuery("select * from rolePermission where roleId= :roleId")
    public abstract List<RolePermission> findByRoleId(@Bind("roleId") String roleId);

    /**
     * Find all role permission associations for provided permission id.
     * 
     * @param permissionId
     * @return list of matching role permissions associations.
     */
    @SqlQuery("select * from rolePermission where permissionId= :permissionId")
    public abstract List<RolePermission> findByPermissionId(@Bind("permissionId") String permissionId);

    /**
     * Find all permission ids for provided role id.
     * 
     * @param roleId
     * @return list of permission ids
     */
    @SqlQuery("select permissionId from rolePermission where roleId= :roleId")
    public abstract List<String> getPermissionIdsByRoleId(@Bind("roleId") String roleId);

    /**
     * Find all role ids for provided permission id.
     * 
     * @param permissionId
     * @return list of role ids
     */
    @SqlQuery("select roleId from rolePermission where permissionId= :permissionId")
    public abstract List<String> getRoleIdsByPermissionId(@Bind("permissionId") String permissionId);

    /**
     * Find all permission ids for provided role ids.
     * 
     * @param roleIds
     * @return list of permission ids
     */
    @SqlQuery("select permissionId from rolePermission where roleId in (<roleIds>)")
    public abstract List<String> getPermissionIdsByRoleIds(@BindIn("roleIds") List<String> roleIds);

    /**
     * Find all role ids for provided permission ids.
     * 
     * @param permissionIds
     * @return list of role ids
     */
    @SqlQuery("select roleId from rolePermission where permissionId in (<permissionIds>)")
    public abstract List<String> getRoleIdsByPermissionIds(@BindIn("permissionIds") List<String> permissionIds);

    /**
     * Delete all role permission association for provided role id.
     * 
     * @param roleId
     * @return count of associations deleted
     */
    @SqlQuery("delete from rolePermission where roleId= :roleId")
    public abstract int deleteByRoleId(@Bind("roleId") String roleId);

    /**
     * Delete all role permission association for provided permission id.
     * 
     * @param permissionId
     * @return count of associations deleted
     */
    @SqlQuery("delete from rolePermission where permissionId= :permissionId")
    public abstract int deleteByPermissionId(@Bind("permissionId") String permissionId);
}

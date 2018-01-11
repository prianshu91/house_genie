package com.promelle.user.management.impl;

import java.util.List;

import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;
import com.promelle.user.dao.PermissionDao;
import com.promelle.user.dao.RoleDao;
import com.promelle.user.dao.RolePermissionDao;
import com.promelle.user.dto.Permission;
import com.promelle.user.dto.Role;
import com.promelle.user.management.RoleManagement;

/**
 * This interface is intended for providing interactions between dao classes related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultRoleManagement implements RoleManagement {

    private RoleDao roleDao;
    private RolePermissionDao rolePermissionDao;
    private PermissionDao permissionDao;

    public DefaultRoleManagement(RoleDao roleDao, RolePermissionDao rolePermissionDao, PermissionDao permissionDao) {
        super();
        this.roleDao = roleDao;
        this.rolePermissionDao = rolePermissionDao;
        this.permissionDao = permissionDao;
    }

    @Override
    public List<Role> list(AbstractRequestTracker requestTracker, Paging paging, SortRules sortRules) {
        return roleDao.findAll();
    }

    @Override
    public List<Permission> getRolePermissions(AbstractRequestTracker requestTracker, List<String> roleIds) {
        List<String> permissionIds = rolePermissionDao.getPermissionIdsByRoleIds(roleIds);
        return permissionIds != null && permissionIds.isEmpty() ? null : permissionDao.findByIds(permissionIds);
    }

    @Override
    public Role findRoleByCode(AbstractRequestTracker requestTracker, String code) {
        return roleDao.findByCode(code);
    }

}

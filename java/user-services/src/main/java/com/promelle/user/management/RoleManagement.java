package com.promelle.user.management;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;
import com.promelle.user.dto.Permission;
import com.promelle.user.dto.Role;

/**
 * This interface is intended for providing interactions between dao classes related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface RoleManagement {

    List<Role> list(AbstractRequestTracker requestTracker, Paging paging, SortRules sortRules) throws AbstractException;

    List<Permission> getRolePermissions(AbstractRequestTracker requestTracker, List<String> roleIds)
            throws AbstractException;

    Role findRoleByCode(AbstractRequestTracker requestTracker, String code) throws AbstractException;

}

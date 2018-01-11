package com.promelle.user.management.impl;

import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.user.dao.PermissionDao;
import com.promelle.user.dto.Permission;
import com.promelle.user.management.PermissionManagement;

/**
 * This interface is intended for providing interactions between dao classes related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultPermissionManagement implements PermissionManagement {

    private PermissionDao permissionDao;

    public DefaultPermissionManagement(PermissionDao permissionDao) {
        super();
        this.permissionDao = permissionDao;
    }

    @Override
    public PagedList<Permission> list(AbstractRequestTracker requestTracker, Paging paging, SortRules sortRules) {
        return permissionDao.list(requestTracker, new SearchFilter(), paging, sortRules);
    }

}

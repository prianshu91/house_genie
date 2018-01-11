package com.promelle.user.management;

import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.user.dto.Permission;

/**
 * This interface is intended for providing interactions between dao classes related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@FunctionalInterface
public interface PermissionManagement {

    PagedList<Permission> list(AbstractRequestTracker requestTracker, Paging paging, SortRules sortRules)
            throws AbstractException;

}

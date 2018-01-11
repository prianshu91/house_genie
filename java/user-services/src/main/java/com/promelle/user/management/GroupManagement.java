package com.promelle.user.management;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.user.dto.Group;
import com.promelle.user.dto.GroupRole;
import com.promelle.user.dto.Role;

/**
 * This interface is intended for providing interactions between dao classes related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface GroupManagement {

    String addGroup(AbstractRequestTracker requestTracker, Group group) throws AbstractException;

    List<Role> getGroupRoles(AbstractRequestTracker requestTracker, String groupId) throws AbstractException;

    List<Role> getGroupNewRoles(AbstractRequestTracker requestTracker, String groupId) throws AbstractException;

    List<GroupRole> addGroupRoles(AbstractRequestTracker requestTracker, String groupId, List<String> roleIds)
            throws AbstractException;

    boolean deleteGroup(AbstractRequestTracker requestTracker, String groupId) throws AbstractException;

    boolean deleteGroupRole(AbstractRequestTracker requestTracker, String groupId, String roleId) throws AbstractException;

    List<Group> getAccountGroups(AbstractRequestTracker requestTracker, String accountId) throws AbstractException;

}

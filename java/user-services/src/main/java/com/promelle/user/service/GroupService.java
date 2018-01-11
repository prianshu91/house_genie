package com.promelle.user.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface GroupService {

    /**
     * Get the list of all roles for the provided group id.
     * 
     * @param requestId
     * @param portalName
     * @param groupId
     * @return response
     * @throws AbstractException
     */
    Response getGroupRoles(String groupId, HttpServletRequest request) throws AbstractException;

    /**
     * Get the list of all roles that are not assigned to the provided group id.
     * 
     * @param requestId
     * @param portalName
     * @param groupId
     * @return response
     * @throws AbstractException
     */
    Response getGroupNewRoles(String groupId, HttpServletRequest request) throws AbstractException;

    /**
     * Add new roles to the provided group id.
     * 
     * @param requestId
     * @param portalName
     * @param groupId
     * @param data
     * @return response
     * @throws AbstractException
     */
    Response addGroupRoles(String groupId, String data, HttpServletRequest request) throws AbstractException;

    /**
     * Remove a role from a group.
     * 
     * @param requestId
     * @param portalName
     * @param groupId
     * @param roleId
     * @return response
     * @throws AbstractException
     */
    Response deleteGroupRole(String groupId, String roleId, HttpServletRequest request) throws AbstractException;

    /**
     * Get the list of groups for provided account id.
     * 
     * @param requestId
     * @param portalName
     * @return accountId
     * @throws AbstractException
     */
    Response getAccountGroups(String accountId, HttpServletRequest request) throws AbstractException;

}

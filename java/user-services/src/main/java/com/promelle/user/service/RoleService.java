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
@FunctionalInterface
public interface RoleService {

    /**
     * Get the list of all permissions for provided role id.
     * 
     * @param requestId
     * @param portalName
     * @param roleId
     * @return response
     * @throws AbstractException
     */
    Response getRolePermissions(String roleId, HttpServletRequest request) throws AbstractException;

}

package com.promelle.user.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to account.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public interface AccountService {

    /**
     * Add a new account.
     * 
     * 
     * @return response
     * @throws AbstractException
     */
    Response addAccount(String data, HttpServletRequest request) throws AbstractException;

    /**
     * Check if account exists or not.
     * 
     * @param accountName
     * @return boolean value
     * @throws AbstractException
     */
    Response checkUniqueAccount(String accountName, HttpServletRequest request) throws AbstractException;

    /**
     * Check if portalName exists or not.
     * 
     * @param portalName
     * @return boolean value
     * @throws AbstractException
     */
    Response checkUniquePortal(String portalName, HttpServletRequest request) throws AbstractException;

    /**
     * List sub accounts
     * 
     * @param accountId
     * @return sub accounts
     */
    Response getSubAccounts(String accountId, HttpServletRequest request) throws AbstractException;

    /**
     * Get portal name
     * 
     * @param accountId
     * @return portal name
     */
    Response getPortalName(String accountId, HttpServletRequest request) throws AbstractException;

}

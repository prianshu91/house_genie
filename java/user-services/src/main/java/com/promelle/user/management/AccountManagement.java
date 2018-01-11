package com.promelle.user.management;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.user.dto.Account;
import com.promelle.user.dto.Signup;

/**
 * This interface is intended for providing interactions between dao classes related to manage.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public interface AccountManagement {

    List<Account> getRootAccounts(AbstractRequestTracker requestTracker) throws AbstractException;

    boolean isAccountExists(AbstractRequestTracker requestTracker, String accountName) throws AbstractException;

    Account addAccount(AbstractRequestTracker requestTracker, Signup signup) throws AbstractException;

    int update(AbstractRequestTracker requestTracker, Account dto, SearchFilter filter) throws AbstractException;

    int deleteAccountById(AbstractRequestTracker requestTracker, String id) throws AbstractException;

    Account getAccountById(AbstractRequestTracker requestTracker, String id) throws AbstractException;

    PagedList<Account> list(AbstractRequestTracker requestTracker, Paging paging, SortRules sortRules) throws AbstractException;

    boolean isPortalExists(AbstractRequestTracker requestTracker, String portalName) throws AbstractException;

    List<Account> getSubAccounts(AbstractRequestTracker requestTracker, String accountId) throws AbstractException;

    String getPortalName(AbstractRequestTracker requestTracker, String accountId) throws AbstractException;

}

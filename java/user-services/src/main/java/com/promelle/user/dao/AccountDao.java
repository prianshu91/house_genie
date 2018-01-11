package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.AccountMapper;
import com.promelle.user.dto.Account;

/**
 * This interface is intended for providing interactions with account table.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
@RegisterMapper(AccountMapper.class)
@UseStringTemplate3StatementLocator
public abstract class AccountDao extends AbstractMariaDao<Account, SearchFilter, AccountMapper> {

    public AccountDao() throws AbstractException {
        super(AccountMapper.class);
    }

    /**
     * Get a list for all root accounts.
     * 
     * @return list of all accounts with parent id = null
     */
    @SqlQuery("select * from account where parentId is null")
    public abstract List<Account> getRootAccounts();

    /**
     * Count the no of account matching with the provided name.
     * 
     * @param name
     * @return count
     */
    @SqlQuery("select count(name) from account where name= :name")
    public abstract int getAccountCount(@Bind("name") String name);

    /**
     * Count the no of portal matching with the provided portal name.
     * 
     * @param name
     * @return count
     */
    @SqlQuery("select count(portalName) from account where portalName= :portalName")
    public abstract int getPortalCount(@Bind("portalName") String portalName);

    /**
     * Count the no of portal matching with the provided portal name.
     * 
     * @param name
     * @return count
     */
    @SqlQuery("select * from account where parentId= :accountId and id != :accountId")
    public abstract List<Account> getSubaccounts(@Bind("accountId") String accountId);

}

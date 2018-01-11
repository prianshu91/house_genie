package com.promelle.user.management.impl;

import java.util.List;

import com.promelle.common.config.AbstractConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.user.dao.AccountDao;
import com.promelle.user.dto.Account;
import com.promelle.user.dto.Signup;
import com.promelle.user.management.AccountManagement;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.UUIDUtils;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class DefaultAccountManagement implements AccountManagement {
	private AccountDao accountDao;
	private MessageProducer messageProducer;

	public static final String ACCOUNT_CREATED = AbstractConfiguration
			.getProperty("account.created");
	public static final String ACCOUNT_UPDATED = AbstractConfiguration
			.getProperty("account.updated");
	public static final String ACCOUNT_DELETED = AbstractConfiguration
			.getProperty("account.deleted");

	public DefaultAccountManagement(AccountDao accountDao,
			MessageProducer messageProducer) {
		super();
		this.accountDao = accountDao;
		this.messageProducer = messageProducer;
	}

	@Override
	public List<Account> getRootAccounts(AbstractRequestTracker requestTracker) {
		return accountDao.getRootAccounts();
	}

	@Override
	public boolean isAccountExists(AbstractRequestTracker requestTracker,
			String accountName) {
		return accountDao.getAccountCount(accountName) > 0;
	}

	@Override
	public Account addAccount(AbstractRequestTracker requestTracker,
			Signup signup) throws AbstractException {
		int portalCount = accountDao.getPortalCount(signup.getPortalName());
		signup.setPortalName(portalCount == 0 ? signup.getPortalName() : signup
				.getPortalName() + portalCount);

		Account account = new Account();
		account.setId(UUIDUtils.getUUID());
		account.setName(signup.getAccountName());
		account.setPortalName(signup.getPortalName());
		account.setOwnerId(signup.getId());
		account.setParentId(null);
		account.setStatus(1);
		accountDao.save(requestTracker, account);

		TopicMessage message = new TopicMessage(requestTracker, ACCOUNT_CREATED);
		message.setData(JsonUtils.getJsonString(account));

		messageProducer.sendMessage(message);
		return account;
	}

	@Override
	public int update(AbstractRequestTracker requestTracker, Account dto,
			SearchFilter filter) throws AbstractException {
		return accountDao.update(requestTracker, dto,
				filter == null ? new SearchFilter() : filter);
	}

	@Override
	public int deleteAccountById(AbstractRequestTracker requestTracker,
			String id) throws AbstractException {
		return accountDao.softDelete(requestTracker, id);
	}

	@Override
	public Account getAccountById(AbstractRequestTracker requestTracker,
			String id) {
		return accountDao.findById(requestTracker, id);
	}

	@Override
	public PagedList<Account> list(AbstractRequestTracker requestTracker,
			Paging paging, SortRules sortRules) {
		return accountDao.list(requestTracker, new SearchFilter(), paging,
				sortRules);
	}

	@Override
	public boolean isPortalExists(AbstractRequestTracker requestTracker,
			String portalName) {
		return accountDao.getPortalCount(portalName) > 0;
	}

	@Override
	public List<Account> getSubAccounts(AbstractRequestTracker requestTracker,
			String accountId) {
		return accountDao.getSubaccounts(accountId);
	}

	@Override
	public String getPortalName(AbstractRequestTracker requestTracker,
			String accountId) {
		Account account = accountDao.findById(requestTracker, accountId);
		return account != null ? account.getPortalName() : null;
	}

}

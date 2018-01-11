package com.promelle.user.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;
import com.promelle.user.dto.Account;
import com.promelle.user.dto.Role;
import com.promelle.user.dto.Signup;
import com.promelle.user.dto.User;
import com.promelle.user.filter.UserFilter;
import com.promelle.user.management.AccountManagement;
import com.promelle.user.management.RoleManagement;
import com.promelle.user.management.UserManagement;
import com.promelle.user.service.AccountService;
import com.promelle.utils.HashUtils;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.UUIDUtils;

/**
 * This class is intended for providing services related to account.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultAccountService extends
		AbstractService<Account, SearchFilter> implements AccountService {
	private static final String STATUS = "status";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultAccountService.class.getName());
	private AccountManagement accountManagement;
	private RoleManagement roleManagement;
	private UserManagement userManagement;

	public DefaultAccountService(AccountManagement accountManagement,
			RoleManagement roleManagement, UserManagement userManagement) {
		super(Account.class, SearchFilter.class);
		this.accountManagement = accountManagement;
		this.roleManagement = roleManagement;
		this.userManagement = userManagement;
	}

	@Override
	@POST
	@Path("/register")
	@Timed(name = "Add account#timer")
	public Response addAccount(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = new AbstractRequestTracker(
					request);
			ObjectMapper mapper = new ObjectMapper();
			Signup signup = Signup.parse(mapper.readTree(data));
			if (StringUtils.isBlank(signup.getAccountName())) {
				return onError("Invalid account name");
			}
			Account account = accountManagement.addAccount(requestTracker,
					signup);

			Role adminRole = roleManagement.findRoleByCode(requestTracker,
					"ADMIN_ROLE");

			User user = mapper.convertValue(signup, User.class);
			user.setPassword(HashUtils.encrypt(StringUtils.isNotBlank(user
					.getPassword()) ? user.getPassword() : UUIDUtils.getUUID()
					.substring(0, 5)));
			user.setRoleId(Arrays.asList(adminRole.getId()));
			user.setAccountId(account.getId());

			userManagement.addUser(requestTracker, user);
			userManagement.addUserRoles(requestTracker, user.getId(),
					Arrays.asList(adminRole.getId()));
			return onSuccess(account);
		} catch (Exception e) {
			LOGGER.error("Error creating account", e);
			return onError(e.getMessage());
		}
	}

	@Override
	@Path("/exists")
	@GET
	@Timed(name = "Check Unique Account#timer")
	public Response checkUniqueAccount(
			@QueryParam("accountName") String accountName,
			@Context HttpServletRequest request) throws AbstractException {
		Map<String, Object> map = new HashMap<>();
		map.put(STATUS, accountManagement.isAccountExists(
				new AbstractRequestTracker(request), accountName));
		return Response.ok(JsonUtils.getJsonString(map)).build();
	}

	@Override
	protected Response save(AbstractRequestTracker requestTracker, Account dto)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Response update(AbstractRequestTracker requestTracker,
			Account dto, SearchFilter filter) throws AbstractException {
		accountManagement.update(requestTracker, dto, filter);
		return Response.ok(
				accountManagement.getAccountById(requestTracker, dto.getId()))
				.build();
	}

	@Override
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		accountManagement.deleteAccountById(requestTracker, id);
		UserFilter filter = new UserFilter();
		filter.setAccountId(id);
		List<User> users = userManagement.listUsers(requestTracker, filter,
				null, null).getObjects();
		for (User user : users) {
			userManagement.deleteUser(requestTracker, user.getId());
		}
		return Response.ok().build();
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		return Response
				.ok(accountManagement.getAccountById(requestTracker, id))
				.build();
	}

	@Override
	protected Response list(AbstractRequestTracker requestTracker,
			SearchFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return onSuccess(accountManagement.list(requestTracker, paging,
				sortRules));
	}

	@Override
	@Path("/portalExists")
	@GET
	@Timed(name = "Check Unique Portal#timer")
	public Response checkUniquePortal(
			@QueryParam("portalName") String portalName,
			@Context HttpServletRequest request) throws AbstractException {
		Map<String, Object> map = new HashMap<>();
		map.put(STATUS, accountManagement.isPortalExists(
				new AbstractRequestTracker(request), portalName));
		return Response.ok(JsonUtils.getJsonString(map)).build();
	}

	@Override
	@Path("/{accountId}/subAccounts")
	@GET
	@Timed(name = "Check Unique Portal#timer")
	public Response getSubAccounts(@PathParam("accountId") String accountId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(accountManagement.getSubAccounts(
				new AbstractRequestTracker(request), accountId));
	}

	@Override
	@Path("/{accountId}/portalName")
	@GET
	@Timed(name = "Get Portal Name#timer")
	public Response getPortalName(@PathParam("accountId") String accountId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(accountManagement.getPortalName(
				new AbstractRequestTracker(request), accountId));
	}

}

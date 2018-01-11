package com.promelle.user.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;
import com.promelle.user.dto.Role;
import com.promelle.user.dto.User;
import com.promelle.utils.UUIDUtils;

/**
 * This class is responsible for handling account creation message.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AccountCreateHandler extends UserMessageHandler {

	public static final String ADMIN_ROLE = "ADMIN_ROLE";
	public static final String SIGNUP = "signup";
	public static final String ACCOUNT = "account";
	public static final String ID = "id";
	public static final String USER_ROLE_ADDED = AbstractConfiguration
			.getProperty("user.role.added");

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("account.created"));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		ObjectMapper mapper = new ObjectMapper();
		Map map;
		try {
			map = mapper.readValue(message.getData(), Map.class);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		Map account = (Map) map.get(ACCOUNT);

		Role adminRole = getRoleManagement().findRoleByCode(
				message.getRequestTracker(), ADMIN_ROLE);

		User user = mapper.convertValue((Map) map.get(SIGNUP), User.class);
		user.setRoleId(Arrays.asList(adminRole.getId()));

		user.setId(UUIDUtils.getUUID());

		user.setAccountId((String) account.get(ID));

		getUserManagement().addUser(message.getRequestTracker(), user);
		getUserManagement().addUserRoles(message.getRequestTracker(),
				user.getId(), Arrays.asList(adminRole.getId()));
	}

}

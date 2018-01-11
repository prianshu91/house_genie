package com.promelle.user.management.impl;

import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.address.Address;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.constants.Punctuation;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.user.dao.DeviceDao;
import com.promelle.user.dao.GroupDao;
import com.promelle.user.dao.GroupRoleDao;
import com.promelle.user.dao.PermissionDao;
import com.promelle.user.dao.RoleDao;
import com.promelle.user.dao.RolePermissionDao;
import com.promelle.user.dao.SchoolDao;
import com.promelle.user.dao.UserDao;
import com.promelle.user.dao.UserGroupDao;
import com.promelle.user.dao.UserPermissionDao;
import com.promelle.user.dao.UserRoleDao;
import com.promelle.user.dao.mapper.UserMapper;
import com.promelle.user.dto.Device;
import com.promelle.user.dto.Group;
import com.promelle.user.dto.Permission;
import com.promelle.user.dto.Role;
import com.promelle.user.dto.School;
import com.promelle.user.dto.User;
import com.promelle.user.dto.UserGroup;
import com.promelle.user.dto.UserPermission;
import com.promelle.user.dto.UserRole;
import com.promelle.user.exception.UserException;
import com.promelle.user.filter.DeviceFilter;
import com.promelle.user.filter.SchoolFilter;
import com.promelle.user.filter.UserFilter;
import com.promelle.user.management.UserManagement;
import com.promelle.utils.HashUtils;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.OTPUtils;
import com.promelle.utils.UUIDUtils;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultUserManagement implements UserManagement {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultUserManagement.class.getName());
	private PermissionDao permissionDao;
	private RoleDao roleDao;
	private GroupDao groupDao;
	private RolePermissionDao rolePermissionDao;
	private UserDao userDao;
	private UserRoleDao userRoleDao;
	private UserPermissionDao userPermissionDao;
	private UserGroupDao userGroupDao;
	private GroupRoleDao groupRoleDao;
	private DeviceDao deviceDao;
	private SchoolDao schoolDao;

	private MessageProducer messageProducer;

	private static final String AMAZON_PIC_UPLOAD = AbstractConfiguration
			.getProperty("amazon.pic.uploaded");

	private static final String USER_UPDATED = AbstractConfiguration
			.getProperty("user.updated");
	private static final String USER_DELETED = AbstractConfiguration
			.getProperty("user.deleted");
	private static final String USER_SIGNUP_OTP = AbstractConfiguration
			.getProperty("user.signup.otp");

	private static final String USER_LOGIN = AbstractConfiguration
			.getProperty("user.login");

	private static final String USER_ROLE_ADDED = AbstractConfiguration
			.getProperty("user.role.added");
	private static final String USER_ROLE_DELETED = AbstractConfiguration
			.getProperty("user.role.deleted");

	private static final String USER_GROUP_ADDED = AbstractConfiguration
			.getProperty("user.group.added");
	private static final String USER_GROUP_DELETED = AbstractConfiguration
			.getProperty("user.group.deleted");

	private static final String USER_PERMISSION_ADDED = AbstractConfiguration
			.getProperty("user.permission.added");
	private static final String USER_PERMISSION_DELETED = AbstractConfiguration
			.getProperty("user.permission.deleted");

	private static final String USER_ACTIVATED = AbstractConfiguration
			.getProperty("user.activated");

	// private static final String DEVICE_REGISTERED = AbstractConfiguration
	// .getProperty("device.registered");

	public DefaultUserManagement(PermissionDao permissionDao, RoleDao roleDao,
			GroupDao groupDao, RolePermissionDao rolePermissionDao,
			UserDao userDao, UserRoleDao userRoleDao,
			UserPermissionDao userPermissionDao, UserGroupDao userGroupDao,
			GroupRoleDao groupRoleDao, DeviceDao deviceDao,
			SchoolDao schoolDao, MessageProducer messageProducer) {
		super();
		this.permissionDao = permissionDao;
		this.roleDao = roleDao;
		this.groupDao = groupDao;
		this.rolePermissionDao = rolePermissionDao;
		this.userDao = userDao;
		this.userRoleDao = userRoleDao;
		this.userPermissionDao = userPermissionDao;
		this.userGroupDao = userGroupDao;
		this.groupRoleDao = groupRoleDao;
		this.deviceDao = deviceDao;
		this.schoolDao = schoolDao;
		this.messageProducer = messageProducer;
	}

	@Override
	public User addUser(AbstractRequestTracker requestTracker, User user)
			throws AbstractException {

		// Code change for implementing PEA-648 solution
		UserFilter userFilter = new UserFilter();
		if (StringUtils.isNotBlank(user.getMobile()))
			userFilter.setMobile(user.getMobile());
		userFilter.setUsername(user.getUsername());

		User dbUser = userDao.findOne(requestTracker, userFilter);

		if (StringUtils.isBlank(user.getId())) {
			if (dbUser == null)
				user.setId(UUIDUtils.getUUID());

			user.setPassword(HashUtils.encrypt(StringUtils.isNotBlank(user
					.getPassword()) ? user.getPassword() : UUIDUtils.getUUID()
					.substring(0, 5)));
		}
		if (StringUtils.isBlank(user.getPortalName())) {
			user.setPortalName(requestTracker.getPortalName());
		}

		// Add school ID
		if (StringUtils.isBlank(user.getSchoolId())) {
			user.setSchoolId(addNewSchool(requestTracker, user.getSchool())
					.getId());
		}

		user.setStatus(2);
		if (dbUser != null) {
			userFilter.setId(dbUser.getId());
			userDao.update(requestTracker, user, userFilter);

			// Set ID after update process
			user.setId(dbUser.getId());
		} else {
			userDao.save(requestTracker, user);
		}

		// Save device information to DB
		Device device = new Device();
		device.setUserId(user.getId());
		device.setDeviceId(user.getDeviceId());
		device.setDeviceToken(user.getDeviceToken());
		device.setDeviceType(StringUtils.isBlank(user.getDeviceType()) ? "iPhone"
				: user.getDeviceType());

		updateDevice(requestTracker, device);

		generateUserOTP(requestTracker, user.getId(), USER_SIGNUP_OTP);

		// Send Push Notification Message for testing purpose
		// TopicMessage msg = new TopicMessage(requestTracker,
		// DEVICE_REGISTERED);
		// msg.setData(checkDeviceExists(requestTracker, device).toString());
		// try {
		// messageProducer.sendMessage(msg);
		// } catch (Exception e) {
		// throw new UserException("oops")
		// .setCustomMessage("Something went wrong!");
		// }

		return user;
	}

	@Override
	public User activateUser(AbstractRequestTracker requestTracker,
			UserFilter filter) throws AbstractException {
		User dbUser = userDao.findOne(requestTracker, filter);
		if (dbUser == null) {
			throw new UserException("user.dosesnt.exists")
					.setCustomMessage("User doesn't exist!");
		}
		if (dbUser.getStatus() == 1) {
			throw new UserException("user.already.activated")
					.setCustomMessage("User already Activated!");
		}
		if (dbUser.getStatus() == 0) {
			throw new UserException("user.already.deleted")
					.setCustomMessage("User already Deleted!");
		}
		User user = new User();
		user.setStatus(1);
		UserFilter userFilter = new UserFilter();
		userFilter.setId(dbUser.getId());
		userFilter.setStatus(2);
		if (userDao.update(requestTracker, user, userFilter) > 0) {
			dbUser = userDao.findById(requestTracker, dbUser.getId());
			TopicMessage msg = new TopicMessage(requestTracker, USER_ACTIVATED);
			msg.setData(dbUser.toString());
			messageProducer.sendMessage(msg);
			return populateUser(requestTracker, dbUser);
		}
		return null;
	}

	@Override
	public User authenticate(AbstractRequestTracker requestTracker, User user)
			throws UserException, AbstractException {
		UserFilter filter = new UserFilter();
		filter.setEmail(user.getEmail());
		filter.setMobile(user.getMobile());
		if (StringUtils.isNotBlank(user.getPortalName())) {
			filter.setPortalName(user.getPortalName());
		}
		// Get specific type error so set to be null
		filter.setStatus(null);
		User authenticatUser = userDao.findOne(requestTracker, filter);
		if (authenticatUser == null) {
			throw new UserException("user.dosesnt.exists")
					.setCustomMessage("User doesn't exist!");
		}
		if (authenticatUser.getStatus() == 0) {
			throw new UserException("disabled.user.login.attempt")
					.setCustomMessage("Disabled user login attempt!");
		}
		if (authenticatUser.getStatus() == 2) {
			throw new UserException("deactivated.user.login.attempt")
					.setCustomMessage("Deactivated user login attempt!");
		}
		if (authenticatUser.getStatus() == 3) {
			throw new UserException("blocked.user.login.attempt")
					.setCustomMessage("Blocked user login attempt!");
		}
		LOGGER.info("user=> " + user);
		LOGGER.info("authenticatUser=> " + authenticatUser);
		if (HashUtils.validate(user.getPassword(),
				authenticatUser.getPassword())) {
			TopicMessage msg = new TopicMessage(requestTracker, USER_LOGIN);
			msg.setData(JsonUtils.getJsonString(user));
			try {
				messageProducer.sendMessage(msg);
			} catch (Exception e) {
				throw new UserException("oops")
						.setCustomMessage("Something went wrong!");
			}
			return authenticatUser;
		}
		return null;
	}

	@Override
	public boolean update(AbstractRequestTracker requestTracker, User user,
			UserFilter filter) throws AbstractException {
		if (user.getStatus() != null && user.getStatus() == 2) {
			return deleteUser(requestTracker, user.getId()) > 0;
		}

		// Add school ID
		if (StringUtils.isBlank(user.getSchoolId()) && user.getSchool() != null) {
			user.setSchoolId(addNewSchool(requestTracker, user.getSchool())
					.getId());
		}
		if (userDao.update(requestTracker, user,
				filter == null ? new UserFilter() : filter) > 0) {
			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_DATA, user);
			map.put(TopicMessage.F_FILTER, filter);

			TopicMessage msg = new TopicMessage(requestTracker, USER_UPDATED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
			return true;
		}
		return false;
	}

	@Override
	public List<Role> getUserRoles(AbstractRequestTracker requestTracker,
			String userId) throws AbstractException {
		List<String> roleIds = userRoleDao.getRoleIdsByUserId(userId);
		return roleIds == null || roleIds.isEmpty() ? new ArrayList<>()
				: roleDao.findByIds(roleIds);
	}

	@Override
	public List<Permission> getUserPermissions(
			AbstractRequestTracker requestTracker, String userId)
			throws AbstractException {
		List<String> permissionIds = userPermissionDao
				.getPermissionIdsByUserId(userId);
		return permissionIds == null || permissionIds.isEmpty() ? new ArrayList<>()
				: permissionDao.findByIds(permissionIds);
	}

	@Override
	public List<Group> getUserGroups(AbstractRequestTracker requestTracker,
			String userId) throws AbstractException {
		List<String> groupIds = userGroupDao.getGroupIdsByUserId(userId);
		return groupIds == null || groupIds.isEmpty() ? new ArrayList<>()
				: groupDao.findByIds(groupIds);
	}

	@Override
	public List<Role> getUserNewRoles(AbstractRequestTracker requestTracker,
			String userId, String accountId) {
		List<String> roleIds = userRoleDao.getRoleIdsByUserId(userId);
		if (roleIds == null) {
			roleIds = new ArrayList<>();
		}
		List<String> groupIds = userGroupDao.getGroupIdsByUserId(userId);
		if (groupIds != null && !groupIds.isEmpty()) {
			List<String> ids = groupRoleDao.getRoleIdsByGroupIds(groupIds);
			if (ids != null) {
				roleIds.addAll(ids);
			}
		}
		return roleIds == null || roleIds.isEmpty() ? roleDao.findAll()
				: roleDao.findAllMinusIds(roleIds);
	}

	@Override
	public List<Permission> getUserNewPermissions(
			AbstractRequestTracker requestTracker, String userId,
			String accountId) {
		List<String> permissionIds = userPermissionDao
				.getPermissionIdsByUserId(userId);
		if (permissionIds == null) {
			permissionIds = new ArrayList<>();
		}
		List<String> roleIds = userRoleDao.getRoleIdsByUserId(userId);
		if (roleIds == null) {
			roleIds = new ArrayList<>();
		}
		List<String> groupIds = userGroupDao.getGroupIdsByUserId(userId);
		if (groupIds != null && !groupIds.isEmpty()) {
			List<String> ids = groupRoleDao.getRoleIdsByGroupIds(groupIds);
			if (ids != null) {
				roleIds.addAll(ids);
			}
		}
		if (!roleIds.isEmpty()) {
			List<String> ids = rolePermissionDao
					.getPermissionIdsByRoleIds(roleIds);
			if (ids != null) {
				permissionIds.addAll(ids);
			}
		}
		return permissionIds.isEmpty() ? permissionDao.findAll()
				: permissionDao.findAllMinusIds(permissionIds);
	}

	@Override
	public List<Group> getUserNewGroups(AbstractRequestTracker requestTracker,
			String userId, String accountId) {
		List<String> groupIds = userGroupDao.getGroupIdsByUserId(userId);
		return groupIds == null || groupIds.isEmpty() ? groupDao
				.findByAccountId(accountId) : groupDao.findByAccountIdMinusIds(
				accountId, groupIds);
	}

	@Override
	public List<UserRole> addUserRoles(AbstractRequestTracker requestTracker,
			String userId, List<String> roleIds) throws AbstractException {
		List<UserRole> userRoles = new ArrayList<>();
		for (String roleId : roleIds) {
			UserRole userRole = new UserRole();
			userRole.setId(UUIDUtils.getUUID());
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			userRoleDao.save(requestTracker, userRole);
			userRoles.add(userRole);
		}
		User user = new User();
		user.setRoleId(roleIds);
		UserFilter userFilter = new UserFilter();
		userFilter.setId(userId);

		Map<String, Object> map = new HashMap<>();
		map.put(TopicMessage.F_DATA, user);
		map.put(TopicMessage.F_FILTER, userFilter);

		TopicMessage msg = new TopicMessage(requestTracker, USER_ROLE_ADDED);
		msg.setData(JsonUtils.getJsonString(map));
		messageProducer.sendMessage(msg);
		return userRoles;
	}

	@Override
	public List<UserPermission> addUserPermissions(
			AbstractRequestTracker requestTracker, String userId,
			List<String> permissionIds) throws AbstractException {
		List<UserPermission> userPermissions = new ArrayList<>();
		for (String permissionId : permissionIds) {
			UserPermission userPermission = new UserPermission();
			userPermission.setId(UUIDUtils.getUUID());
			userPermission.setUserId(userId);
			userPermission.setPermissionId(permissionId);
			System.out.println(userPermission);
			userPermissionDao.save(requestTracker, userPermission);
			userPermissions.add(userPermission);
		}
		TopicMessage msg = new TopicMessage(requestTracker,
				USER_PERMISSION_ADDED);
		msg.setData(userPermissions.toString());
		messageProducer.sendMessage(msg);
		return userPermissions;
	}

	@Override
	public List<UserGroup> addUserGroups(AbstractRequestTracker requestTracker,
			String userId, List<String> groupIds) throws AbstractException {
		List<UserGroup> userGroups = new ArrayList<>();
		for (String groupId : groupIds) {
			UserGroup userGroup = new UserGroup();
			userGroup.setId(UUIDUtils.getUUID());
			userGroup.setUserId(userId);
			userGroup.setGroupId(groupId);
			userGroupDao.save(requestTracker, userGroup);
			userGroups.add(userGroup);
		}
		TopicMessage msg = new TopicMessage(requestTracker, USER_GROUP_ADDED);
		msg.setData(userGroups.toString());
		messageProducer.sendMessage(msg);
		return userGroups;
	}

	@Override
	public UserRole deleteUserRole(AbstractRequestTracker requestTracker,
			String userId, String roleId) throws AbstractException {
		UserRole userRole = userRoleDao.findByUserIdRoleId(userId, roleId);
		if (userRole != null) {
			userRoleDao.deleteUserRole(userId, roleId);

			UserFilter userFilter = new UserFilter();
			userFilter.setId(userId);
			userFilter.setRoleId(roleId);

			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_FILTER, userFilter);

			TopicMessage msg = new TopicMessage(requestTracker,
					USER_ROLE_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
		}
		return userRole;
	}

	@Override
	public UserPermission deleteUserPermission(
			AbstractRequestTracker requestTracker, String userId,
			String permissionId) throws AbstractException {
		UserPermission userPermission = userPermissionDao
				.findByUserIdPermissionId(userId, permissionId);
		if (userPermission != null) {
			userPermissionDao.deleteUserPermission(userId, permissionId);

			UserFilter userFilter = new UserFilter();
			userFilter.setId(userId);
			userFilter.setPermissionId(permissionId);

			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_FILTER, userFilter);

			TopicMessage msg = new TopicMessage(requestTracker,
					USER_PERMISSION_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
		}
		return userPermission;
	}

	@Override
	public UserGroup deleteUserGroup(AbstractRequestTracker requestTracker,
			String userId, String groupId) throws AbstractException {
		UserGroup userGroup = userGroupDao.findByUserIdGroupId(userId, groupId);
		if (userGroup != null) {
			userGroupDao.deleteUserGroup(userId, groupId);

			UserFilter userFilter = new UserFilter();
			userFilter.setId(userId);
			userFilter.setGroupId(groupId);

			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_FILTER, userFilter);

			TopicMessage msg = new TopicMessage(requestTracker,
					USER_GROUP_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
		}
		return userGroup;
	}

	@Override
	public List<Permission> getUserFinalPermissions(
			AbstractRequestTracker requestTracker, String userId) {
		List<String> permissionIds = userPermissionDao
				.getPermissionIdsByUserId(userId);
		if (permissionIds == null) {
			permissionIds = new ArrayList<>();
		}
		List<String> roleIds = userRoleDao.getRoleIdsByUserId(userId);
		if (roleIds == null) {
			roleIds = new ArrayList<>();
		}
		List<String> groupIds = userGroupDao.getGroupIdsByUserId(userId);
		if (groupIds != null && !groupIds.isEmpty()) {
			List<String> ids = groupRoleDao.getRoleIdsByGroupIds(groupIds);
			if (ids != null) {
				roleIds.addAll(ids);
			}
		}
		if (!roleIds.isEmpty()) {
			List<String> ids = rolePermissionDao
					.getPermissionIdsByRoleIds(roleIds);
			if (ids != null) {
				permissionIds.addAll(ids);
			}
		}
		return permissionIds.isEmpty() ? new ArrayList<>() : permissionDao
				.findByIds(permissionIds);
	}

	@Override
	public void validateUserExists(AbstractRequestTracker requestTracker,
			User user) throws UserException, AbstractException {
		System.out.println("validate user =>" + user);
		UserFilter filter = new UserFilter();
		if (StringUtils.isNotBlank(user.getEmail())) {
			filter.setEmail(user.getEmail());
			User dbUser = userDao.findOne(requestTracker, filter);
			if (dbUser != null
					&& !dbUser.getId().equalsIgnoreCase(user.getId())) {
				throw new UserException("email.already.exists")
						.setCustomMessage("An account with this email already exists!");
			}
			filter.setEmail(null);
		}
		if (StringUtils.isNotBlank(user.getMobile())) {
			filter.setMobile(user.getMobile());
			User dbUser = userDao.findOne(requestTracker, filter);
			if (dbUser != null
					&& !dbUser.getId().equalsIgnoreCase(user.getId())) {
				throw new UserException("mobile.already.exists")
						.setCustomMessage("An account with this mobile number already exists!");
			}
			filter.setMobile(null);
		}
		if (StringUtils.isNotBlank(user.getUsername())) {
			filter.setUsername(user.getUsername());
			User dbUser = userDao.findOne(requestTracker, filter);
			if (dbUser != null
					&& !dbUser.getId().equalsIgnoreCase(user.getId())) {
				throw new UserException("username.already.exists")
						.setCustomMessage("An account with this username already exists!");
			}
			filter.setUsername(null);
		}
	}

	@Override
	public void validateAddUserExists(AbstractRequestTracker requestTracker,
			User user) throws UserException, AbstractException {

		System.out.println("validate user =>" + user);
		UserFilter filter = new UserFilter();
		if (StringUtils.isNotBlank(user.getEmail())) {
			filter.setEmail(user.getEmail());
			User dbUser = userDao.findOne(requestTracker, filter);
			if (dbUser != null
					&& !dbUser.getId().equalsIgnoreCase(user.getId())
					&& dbUser.getStatus() != 2) {
				throw new UserException("email.already.exists")
						.setCustomMessage("An account with this email already exists!");
			}
			filter.setEmail(null);
		}
		if (StringUtils.isNotBlank(user.getMobile())) {
			filter.setMobile(user.getMobile());
			User dbUser = userDao.findOne(requestTracker, filter);
			if (dbUser != null
					&& !dbUser.getId().equalsIgnoreCase(user.getId())
					&& dbUser.getStatus() != 2) {
				throw new UserException("mobile.already.exists")
						.setCustomMessage("An account with this mobile number already exists!");
			}
			filter.setMobile(null);
		}
		if (StringUtils.isNotBlank(user.getUsername())) {
			filter.setUsername(user.getUsername());
			User dbUser = userDao.findOne(requestTracker, filter);
			if (dbUser != null
					&& !dbUser.getId().equalsIgnoreCase(user.getId())
					&& dbUser.getStatus() != 2) {
				throw new UserException("username.already.exists")
						.setCustomMessage("An account with this username already exists!");
			}
			filter.setUsername(null);
		}
	}

	@Override
	public PagedList<User> listUsers(AbstractRequestTracker requestTracker,
			UserFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {

		if (StringUtils.isNotBlank(filter.getSearchText())) {
			String valString = "%" + filter.getSearchText() + "%";
			Map<String, Object> objMap = new HashMap<>();
			objMap.put(UserMapper.USERNAME, valString);
			objMap.put(UserMapper.NAME, valString);

			if (NumberUtils.isNumber(filter.getSearchText()))
				objMap.put(UserMapper.HOME_ADDRESS, valString);

			objMap.put(UserMapper.ORGANIZATION_NAME, valString);

			String queryParams = Punctuation.BRACKET_OPEN.toString()
					+ userDao.searchJoin(UserDao.OR, objMap).toString()
					+ Punctuation.BRACKET_CLOSE.toString();
			filter.setQuery(queryParams);
		}

		// Also add mobile and email for broadcast tab search
		if (StringUtils.isNotBlank(filter.getText())) {
			String valString = "%" + filter.getText() + "%";
			Map<String, Object> objMap = new HashMap<>();
			// objMap.put(UserMapper.USERNAME, valString);
			objMap.put(UserMapper.NAME, valString);
			objMap.put(UserMapper.MOBILE, valString);
			objMap.put(UserMapper.EMAIL, valString);

			String queryParams = Punctuation.BRACKET_OPEN.toString()
					+ userDao.searchJoin(UserDao.OR, objMap).toString()
					+ Punctuation.BRACKET_CLOSE.toString();
			filter.setQuery(queryParams);
		}
		if (filter.getSearchDate() != null) {
			StringJoiner joiner = new StringJoiner(UserDao.AND);
			joiner.add(CREATED_ON + " >= " + filter.getSearchDate());
			joiner.add(CREATED_ON + " < "
					+ (filter.getSearchDate() + (long) (24 * 60 * 60 * 1000)));
			filter.setQuery(joiner.toString());
		}

		PagedList<User> pagedList = userDao.list(requestTracker, filter,
				paging, sortRules);
		populateUsersList(requestTracker, pagedList);
		return pagedList;
	}

	@Override
	public boolean updateProfilePic(AbstractRequestTracker requestTracker,
			String id, String fileName) throws AbstractException {
		if (userDao.updateProfilePic(id, fileName) > 0) {
			TopicMessage msg = new TopicMessage(requestTracker,
					AMAZON_PIC_UPLOAD);
			msg.setData(fileName);
			messageProducer.sendMessage(msg);
			return true;
		}
		return false;
	}

	@Override
	public int deleteUser(AbstractRequestTracker requestTracker, String userId)
			throws AbstractException {
		int status = userDao.softDelete(requestTracker, userId);
		if (status > 0) {
			userRoleDao.deleteByUserId(userId);
			userPermissionDao.deleteByUserId(userId);
			userGroupDao.deleteByUserId(userId);
			TopicMessage msg = new TopicMessage(requestTracker, USER_DELETED);
			msg.setData(userId);
			messageProducer.sendMessage(msg);
		}
		return status;
	}

	@Override
	public User findUser(AbstractRequestTracker requestTracker,
			UserFilter filter) throws AbstractException {
		return populateUser(requestTracker,
				userDao.findOne(requestTracker, filter));
	}

	@Override
	public User findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		return populateUser(requestTracker,
				userDao.findById(requestTracker, id));
	}

	@Override
	public boolean generateUserOTP(AbstractRequestTracker requestTracker,
			String userId, String type) throws AbstractException {
		User user = userDao.findById(requestTracker, userId);
		if (user == null) {
			throw new UserException("user.dosesnt.exists")
					.setCustomMessage("User doesn't exist!");
		}
		user.setOtp(OTPUtils.getOTP(4));
		UserFilter filter = new UserFilter();
		filter.setId(userId);
		if (userDao.update(requestTracker, user, filter) > 0) {
			TopicMessage msg = new TopicMessage(requestTracker, type);
			msg.setData(user.toString());
			messageProducer.sendMessage(msg);
			return true;
		}
		return false;
	}

	@Override
	public void addDevice(AbstractRequestTracker requestTracker, Device device)
			throws AbstractException {

		if (StringUtils.isBlank(device.getUserId())
				|| StringUtils.isBlank(device.getDeviceId())
				|| StringUtils.isBlank(device.getDeviceToken()))
			return;

		// add device related data
		device.setId(UUIDUtils.getUUID());

		deviceDao.save(requestTracker, device);
		LOGGER.info("Device save Successfully => " + device.getId());
	}

	@Override
	public void updateDevice(AbstractRequestTracker requestTracker,
			Device device) throws AbstractException {

		if (StringUtils.isBlank(device.getUserId())
				|| StringUtils.isBlank(device.getDeviceId())
				|| StringUtils.isBlank(device.getDeviceToken()))
			return;

		System.out.println("validate device =>" + device);

		Device dbDevice = checkDeviceExists(requestTracker, device);
		if (dbDevice != null) {
			// deviceDao.updateDeviceToken(dbDevice.getId(),
			// device.getDeviceToken());

			Device addDevice = new Device();
			addDevice.setDeviceToken(device.getDeviceToken());
			addDevice.setStatus(1);
			addDevice.setModifiedOn(requestTracker.getRequestTimestamp());

			DeviceFilter filter = new DeviceFilter();
			filter.setId(dbDevice.getId());

			deviceDao.update(requestTracker, addDevice, filter);

			LOGGER.info("Device updated Successful => " + device);
		} else {
			addDevice(requestTracker, device);
		}
	}

	@Override
	public void updateDeviceStatus(AbstractRequestTracker requestTracker,
			Device device) throws AbstractException {

		if (StringUtils.isBlank(device.getUserId())
				|| StringUtils.isBlank(device.getDeviceId()))
			return;

		Device dbDevice = checkDeviceExists(requestTracker, device);
		if (dbDevice != null) {
			deviceDao.updateDeviceStatus(dbDevice.getId(), 0);
		} else {
			device.setStatus(0);
			addDevice(requestTracker, device);
		}
	}

	@Override
	public Device checkDeviceExists(AbstractRequestTracker requestTracker,
			Device device) throws AbstractException {

		DeviceFilter filter = new DeviceFilter();
		filter.setUserId(device.getUserId());
		filter.setDeviceId(device.getDeviceId());

		return deviceDao.findOne(requestTracker, filter);
	}

	@Override
	public int updateUserEmail(AbstractRequestTracker requestTracker,
			User user, String id) throws AbstractException {
		return userDao.updateEmail(id, user.getEmail());
	}

	@Override
	public School addNewSchool(AbstractRequestTracker requestTracker,
			School school) throws AbstractException {

		School dbSchool = checkSchoolExists(requestTracker, school);
		if (dbSchool != null) {
			return dbSchool;
		}

		if (school.getStatus() == null) {
			school.setStatus(0);
		}

		// For adding schoolPod
		if (StringUtils.isBlank(school.getSchoolPodName())
				&& StringUtils.isNotBlank(school.getSchoolName()))
			school.setSchoolPodName(school.getSchoolName());

		schoolDao.save(requestTracker, school);

		return checkSchoolExists(requestTracker, school);
	}

	@Override
	public School checkSchoolExists(AbstractRequestTracker requestTracker,
			School school) throws AbstractException {

		if (school == null)
			return new School();

		if (StringUtils.isBlank(school.getSchoolName())
				&& StringUtils.isBlank(school.getId()))
			return new School();

		SchoolFilter filter = new SchoolFilter();
		filter.setId(StringUtils.isBlank(school.getId()) ? "" : school.getId());
		filter.setSchoolName(StringUtils.isBlank(school.getSchoolName()) ? ""
				: school.getSchoolName());

		School dbSchool = schoolDao.findOne(requestTracker, filter);
		if (dbSchool != null && dbSchool.getSchoolAddress() == null
				&& school.getSchoolAddress() != null)
			schoolDao.update(requestTracker, school, filter);

		return schoolDao.findOne(requestTracker, filter);
	}

	@Override
	public PagedList<School> listofActiveSchools(
			AbstractRequestTracker requestTracker, SchoolFilter schoolFilter,
			Paging paging, SortRules sortRules) throws AbstractException {
		return schoolDao.list(requestTracker, schoolFilter, paging, sortRules);
	}

	@Override
	public void populateUsersList(AbstractRequestTracker requestTracker,
			PagedList<User> usersList) throws AbstractException {

		List<User> users = usersList.getObjects();
		for (User user : users) {
			populateUser(requestTracker, user);
		}
	}

	@Override
	public User populateUser(AbstractRequestTracker requestTracker, User user)
			throws AbstractException {

		if (user == null)
			return user;

		School school = null;
		if (StringUtils.isNotBlank(user.getSchoolId()))
			school = schoolDao.findById(requestTracker, user.getSchoolId());

		if (school != null) {
			user.setSchool(school);
		} else {
			school = new School();
			String orgName = user.getOrganizationName();

			if (StringUtils.isBlank(orgName))
				return user;

			if (orgName.contains("#$#&#")) {
				String addrDelim = "#$#&#";
				school.setSchoolName(orgName.substring(0,
						orgName.indexOf(addrDelim)));

				// Get school address from orgName
				int beginIndex = orgName.indexOf(addrDelim)
						+ addrDelim.length();
				String subOrgName = orgName.substring(beginIndex);

				String orgAddress = subOrgName.substring(0,
						subOrgName.indexOf(addrDelim));
				String[] addrStrings = orgAddress.split(",");

				Address address = new Address();
				if (addrStrings.length > 2) {
					address.setAddressLine1(addrStrings[0].trim());
					address.setCity(addrStrings[1].trim());
					address.setState(addrStrings[2].trim());
				}
				school.setSchoolAddress(address);

			} else if (orgName.toLowerCase().contains("harker")) {
				school.setSchoolName("The Harker School - Upper School Campus");
			} else {
				school.setSchoolName(orgName);
			}

			School dbSchool = addNewSchool(requestTracker, school);
			userDao.updateSchoolId(user.getId(), dbSchool.getId());
			user.setSchoolId(dbSchool.getId());
			user.setSchool(dbSchool);
		}

		if (user.getSchool().getSchoolAddress() != null
				&& StringUtils.isBlank(user.getSchool().getSchoolAddress()
						.getZipCode()))
			user.getSchool().getSchoolAddress().setZipCode(" ");

		return user;
	}

	@Override
	public School findSchoolById(AbstractRequestTracker requestTracker,
			String schoolId, String schoolPod) throws AbstractException {
		School school = schoolDao.findById(requestTracker, schoolId);

		if (StringUtils.isBlank(schoolPod))
			return school;

		// If school pod information request send by user

		UserFilter filter = new UserFilter();
		filter.setSchoolId(schoolId);

		List<User> podMembersList = userDao.list(requestTracker, filter)
				.getObjects();
		school.setPodMembers(podMembersList);

		return school;
	}

	@Override
	public boolean updateSchoolPodLogo(AbstractRequestTracker requestTracker,
			String id, String fileName) throws AbstractException {
		return schoolDao.updateSchoolPodLogo(id, fileName) > 0;
	}

	public int updateSchool(AbstractRequestTracker requestTracker,
			School school, SchoolFilter filter) throws AbstractException {
		return schoolDao.update(requestTracker, school, filter);
	}
}

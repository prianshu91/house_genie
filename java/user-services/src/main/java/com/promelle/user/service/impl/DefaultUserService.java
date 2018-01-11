package com.promelle.user.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.amazon.upload.S3Uploader;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortOrder;
import com.promelle.sort.SortRule;
import com.promelle.sort.SortRules;
import com.promelle.user.dao.mapper.SchoolMapper;
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
import com.promelle.user.filter.SchoolFilter;
import com.promelle.user.filter.UserFilter;
import com.promelle.user.management.UserManagement;
import com.promelle.user.service.UserService;
import com.promelle.utils.HashUtils;
import com.promelle.utils.JsonUtils;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultUserService extends AbstractService<User, UserFilter>
		implements UserService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultUserService.class.getName());
	private static final String PROFILE_PIC = "profilepic";
	private static final String SCHOOL_POD_LOGO = "schoolPodLogo";
	private UserManagement userManagement;
	private S3Uploader s3Uploader;

	public DefaultUserService(UserManagement userManagement,
			S3Uploader s3Uploader) {
		super(User.class, UserFilter.class);
		this.userManagement = userManagement;
		this.s3Uploader = s3Uploader;
	}

	@Override
	@Path("/login")
	@POST
	@Timed(name = "Login#timer")
	public Response login(String data, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		Device device;
		User user;
		boolean isAdmin = false;
		try {
			user = new ObjectMapper().readValue(data, User.class);
			device = new ObjectMapper().readValue(data, Device.class);

			isAdmin = user.getAdmin();
		} catch (Exception e) {
			LOGGER.error("Error in login", e);
			return onError("bad.request", Arrays.asList("Bad request!"));
		}
		if (StringUtils.isBlank(user.getPassword())) {
			return onError("password.required",
					Arrays.asList("Password required!"));
		}
		if ((StringUtils.isBlank(user.getEmail()) && StringUtils.isBlank(user
				.getMobile()))) {
			return onError("bad.request",
					Arrays.asList("Either email or mobile no required!"));
		}
		LOGGER.info("Login User => " + user);

		try {
			user = userManagement.authenticate(requestTracker, user);
		} catch (AbstractException e) {
			return onError(e);
		}
		if (user == null) {
			return onError("user.auth.failed",
					Arrays.asList("User authorization failed!"));
		}
		user.setPassword(null);
		List<String> permissions = new ArrayList<>();
		List<Permission> permissionObjs = userManagement
				.getUserFinalPermissions(new AbstractRequestTracker(request),
						user.getId());
		if (CollectionUtils.isNotEmpty(permissionObjs)) {
			for (Permission permission : permissionObjs) {
				permissions.add(permission.getCode());
			}
		}
		user.setPermissions(permissions);

		// User access control changes
		if (isAdmin) {
			if (permissions != null && permissions.size() > 0) {
				for (String prmsn : permissions) {
					if (prmsn.equalsIgnoreCase("USER_MANAGE")
							|| prmsn.equalsIgnoreCase("ACCOUNT_MANAGE"))
						return onSuccess(userManagement.populateUser(
								requestTracker, user));
				}
			}
			return onError("unauth.user", Arrays.asList("Unauthorized user!"));
		} else {
			LOGGER.info("User Auth Successful => " + user);
			device.setUserId(user.getId());
			userManagement.updateDevice(requestTracker, device);
			return onSuccess(userManagement.populateUser(requestTracker, user));
		}
	}

	@Override
	@Timed(name = "Add user#timer")
	protected Response save(AbstractRequestTracker requestTracker, User user)
			throws AbstractException {
		try {
			userManagement.validateAddUserExists(requestTracker, user);
		} catch (UserException e) {
			return onError(e);
		}
		userManagement.addUser(requestTracker, user);
		return onSuccess(user);
	}

	@Override
	@Timed(name = "Update user#timer")
	protected Response update(AbstractRequestTracker requestTracker, User user,
			UserFilter filter) throws AbstractException {
		// We should always call delete user or enable by its specific service.
		user.setStatus(null);
		user.setPassword(null);
		if (StringUtils.isBlank(user.getId())
				&& StringUtils.isNotBlank(filter.getId())) {
			user.setId(filter.getId());
		}
		try {
			userManagement.validateUserExists(requestTracker, user);
		} catch (UserException e) {
			return onError(e);
		}
		if (userManagement.update(requestTracker, user, filter)) {
			User dbUser = userManagement.findUser(requestTracker, filter);
			return onSuccess(dbUser);
		}
		return onError(new AbstractException());
	}

	@Override
	@Timed(name = "Delete user#timer")
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		userManagement.deleteUser(requestTracker, id);
		return onSuccess(id);
	}

	@Override
	@Path("/enable/{id}")
	@PUT
	@Timed(name = "Enable user#timer")
	public Response enableUser(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		UserFilter filter = new UserFilter();
		filter.setId(id);
		// Temp change
		// filter.setStatus(0);
		User user = new User();
		user.setStatus(1);
		userManagement
				.update(new AbstractRequestTracker(request), user, filter);
		return onSuccess(user);
	}

	@Override
	@Path("/disable/{id}")
	@PUT
	@Timed(name = "Disable user#timer")
	public Response disableUser(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		UserFilter filter = new UserFilter();
		filter.setId(id);
		User user = new User();
		// status 3 - blocked/disabled user.
		user.setStatus(3);
		userManagement
				.update(new AbstractRequestTracker(request), user, filter);
		return onSuccess(user);
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		UserFilter filter = new UserFilter();
		filter.setId(id);
		return onSuccess(userManagement.findUser(requestTracker, filter));
	}

	@Override
	@Timed(name = "List users#timer")
	protected Response list(AbstractRequestTracker requestTracker,
			UserFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return onSuccess(userManagement.listUsers(requestTracker, filter,
				paging, sortRules));
	}

	@Override
	@Path("/activate")
	@POST
	@Timed(name = "Activate User#timer")
	public Response activate(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			UserFilter userFilter = new ObjectMapper().readValue(data,
					UserFilter.class);
			if (StringUtils.isBlank(userFilter.getId())
					&& StringUtils.isBlank(userFilter.getEmail())) {
				return onError("bad.request", Arrays.asList("Bad request!"));
			}
			try {
				return onSuccess(userManagement.activateUser(
						new AbstractRequestTracker(request), userFilter));
			} catch (UserException e) {
				return onError(e);
			}
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	@Path("/{userId}/roles")
	@GET
	@Timed(name = "Get user roles#timer")
	public Response getUserRoles(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserRoles(
				new AbstractRequestTracker(request), userId));
	}

	@Override
	@Path("/{userId}/permissions")
	@GET
	@Timed(name = "Get user permissions#timer")
	public Response getUserPermissions(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserPermissions(
				new AbstractRequestTracker(request), userId));
	}

	@Override
	@Path("/{userId}/groups")
	@GET
	@Timed(name = "Get user groups#timer")
	public Response getUserGroups(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserGroups(
				new AbstractRequestTracker(request), userId));
	}

	@Override
	@Path("/{userId}/roles")
	@POST
	@Timed(name = "Add user roles#timer")
	public Response addUserRoles(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			return onSuccess(userManagement.addUserRoles(
					new AbstractRequestTracker(request),
					userId,
					Arrays.asList(new ObjectMapper().readTree(data)
							.get("roleIds").asText().split(","))));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	@Path("/{userId}/permissions")
	@POST
	@Timed(name = "Add user permissions#timer")
	public Response addUserPermissions(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			return onSuccess(userManagement.addUserPermissions(
					new AbstractRequestTracker(request),
					userId,
					Arrays.asList(new ObjectMapper().readTree(data)
							.get("permissionIds").asText().split(","))));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	@Path("/{userId}/groups")
	@POST
	@Timed(name = "Add user groups#timer")
	public Response addUserGroups(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			return onSuccess(userManagement.addUserGroups(
					new AbstractRequestTracker(request),
					userId,
					Arrays.asList(new ObjectMapper().readTree(data)
							.get("groupIds").asText().split(","))));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	@Path("/{userId}/role/{roleId}")
	@DELETE
	@Timed(name = "Delete user role#timer")
	public Response deleteUserRole(@PathParam("userId") String userId,
			@PathParam("roleId") String roleId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.deleteUserRole(
				new AbstractRequestTracker(request), userId, roleId));
	}

	@Override
	@Path("/{userId}/permission/{permissionId}")
	@DELETE
	@Timed(name = "Delete user permission#timer")
	public Response deleteUserPermission(@PathParam("userId") String userId,
			@PathParam("permissionId") String permissionId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.deleteUserPermission(
				new AbstractRequestTracker(request), userId, permissionId));
	}

	@Override
	@Path("/{userId}/group/{groupId}")
	@DELETE
	@Timed(name = "Delete user group#timer")
	public Response deleteUserGroup(@PathParam("userId") String userId,
			@PathParam("groupId") String groupId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.deleteUserGroup(
				new AbstractRequestTracker(request), userId, groupId));
	}

	@Override
	@Path("/{userId}/account/{accountId}/roles/new")
	@GET
	@Timed(name = "Get user new roles#timer")
	public Response getUserNewRoles(@PathParam("userId") String userId,
			@PathParam("accountId") String accountId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserNewRoles(
				new AbstractRequestTracker(request), userId, accountId));
	}

	@Override
	@Path("/{userId}/account/{accountId}/permissions/new")
	@GET
	@Timed(name = "Get user new permissions#timer")
	public Response getUserNewPermissions(@PathParam("userId") String userId,
			@PathParam("accountId") String accountId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserNewPermissions(
				new AbstractRequestTracker(request), userId, accountId));
	}

	@Override
	@Path("/{userId}/account/{accountId}/groups/new")
	@GET
	@Timed(name = "Get user new groups#timer")
	public Response getUserNewGroups(@PathParam("userId") String userId,
			@PathParam("accountId") String accountId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserNewGroups(
				new AbstractRequestTracker(request), userId, accountId));
	}

	@Override
	@Path("/{userId}/finalPermissions")
	@GET
	@Timed(name = "Get user final permisssions#timer")
	public Response getUserFinalPermissions(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.getUserFinalPermissions(
				new AbstractRequestTracker(request), userId));
	}

	@Override
	@Path("/forgetPassword")
	@GET
	@Timed(name = "Forget user password#timer")
	public Response forgetPassword(@QueryParam("email") String email,
			@Context HttpServletRequest request) throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		if (StringUtils.isBlank(email)) {
			return onError("bad.request", Arrays.asList("Bad request!"));
		}
		UserFilter filter = new UserFilter();
		filter.setEmail(email);
		User user = userManagement.findUser(requestTracker, filter);
		if (user == null) {
			return onError(new UserException("user.dosesnt.exists")
					.setCustomMessage("User doesn't exist!"));
		}
		// TODO move this to management
		userManagement.generateUserOTP(requestTracker, user.getId(),
				AbstractConfiguration.getProperty("forgot.password.otp"));
		return onSuccess(email);
	}

	@Override
	@Path("/resetPassword")
	@POST
	@Timed(name = "Reset password#timer")
	public Response resetPassword(String data,
			@Context HttpServletRequest request) throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readTree(data);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		String userId = JsonUtils.getStringValue(node, "userId", null);
		String email = JsonUtils.getStringValue(node, "email", null);
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(email)) {
			return onError("bad.request", Arrays.asList("Bad request!"));
		}
		String password = JsonUtils.getStringValue(node, "password", null);
		String otp = JsonUtils.getStringValue(node, "otp", null);
		String newPassword = JsonUtils
				.getStringValue(node, "newPassword", null);
		UserFilter filter = new UserFilter();
		if (StringUtils.isNotBlank(userId)) {
			filter.setId(userId);
		}
		if (StringUtils.isNotBlank(email)) {
			filter.setEmail(email);
		}
		User user = userManagement.findUser(requestTracker, filter);
		if (user != null) {
			if (StringUtils.isNotBlank(password)) {
				if (HashUtils.validate(password, user.getPassword())) {
					// hash user password if not hashed already
					user.setPassword(HashUtils.encrypt(newPassword));
					userManagement.update(requestTracker, user, filter);
					return onSuccess(user);
				} else {
					// old password mismatch
					return onError("user.password.mismatch");
				}
			} else {
				if (otp.equalsIgnoreCase(user.getOtp())) {
					// hash user password if not hashed already
					user.setPassword(HashUtils.encrypt(newPassword));
					userManagement.update(requestTracker, user, filter);
					return onSuccess(user);
				} else {
					// old password mismatch
					return onError("user.otp.mismatch");
				}
			}
		} else {
			// no user with given id
			return onError("no_user_with_id");
		}
	}

	@Override
	@Path("/uploadPicture")
	@POST
	@Timed(name = "Upload picture#timer")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadPicture(
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("id") String id, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		// Get the file extension
		String fileToUpload = fileDetail.getFileName();
		String extension = fileToUpload.substring(
				fileToUpload.lastIndexOf("."), fileToUpload.length());
		String fileName;
		try {
			fileName = s3Uploader.upload(inputStream, PROFILE_PIC, id,
					extension, true);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		userManagement.updateProfilePic(requestTracker, id, fileName);
		return Response.status(200).entity(fileName).build();
	}

	@Path("{userId}/updatePermissions")
	@PUT
	@Timed(name = "Add user permissions#timer")
	public Response updateUserPermissions(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		List<Permission> oldUserPermissions = userManagement
				.getUserPermissions(requestTracker, userId);
		if (oldUserPermissions != null && !oldUserPermissions.isEmpty()) {
			for (Permission oldUserPermission : oldUserPermissions) {
				try {
					userManagement.deleteUserPermission(requestTracker, userId,
							oldUserPermission.getId());
				} catch (AbstractException e) {
					LOGGER.error("Error in updateUserPermissions()", e);
				}
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> permissions;
		try {
			permissions = mapper.readValue(data, List.class);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		List<String> currentPermissions = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(permissions)) {
			List<UserPermission> userPermissions = userManagement
					.addUserPermissions(requestTracker, userId, permissions);
			for (UserPermission permission : userPermissions) {
				currentPermissions.add(permission.getId());
			}
		}
		return onSuccess(currentPermissions);
	}

	@Path("{userId}/updateRoles")
	@PUT
	@Timed(name = "Add user roles#timer")
	public Response updateUserRoles(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		List<Role> oldUserRoles = userManagement.getUserRoles(requestTracker,
				userId);
		if (oldUserRoles != null && !oldUserRoles.isEmpty()) {
			for (Role oldUserRole : oldUserRoles) {
				try {
					userManagement.deleteUserRole(requestTracker, userId,
							oldUserRole.getId());
				} catch (AbstractException e) {
					LOGGER.error("Error in updateUserRoles()", e);
				}
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> roles;
		try {
			roles = mapper.readValue(data, List.class);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		List<String> currentRoles = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(roles)) {
			List<UserRole> userRoles = userManagement.addUserRoles(
					requestTracker, userId, roles);
			for (UserRole user : userRoles) {
				currentRoles.add(user.getRoleId());
			}
		}
		return onSuccess(currentRoles);
	}

	@Path("{userId}/updateGroups")
	@PUT
	@Timed(name = "Add user roles#timer")
	public Response updateUserGroups(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		List<Group> oldUserGroups = userManagement.getUserGroups(
				requestTracker, userId);
		if (oldUserGroups != null && !oldUserGroups.isEmpty()) {
			for (Group oldUserGroup : oldUserGroups) {
				try {
					userManagement.deleteUserGroup(requestTracker, userId,
							oldUserGroup.getId());
				} catch (AbstractException e) {
					LOGGER.error("Error in updateUserGroups()", e);
				}
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> groups;
		try {
			groups = mapper.readValue(data, List.class);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		List<String> currentGroups = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(groups)) {
			List<UserGroup> userGroups = userManagement.addUserGroups(
					requestTracker, userId, groups);
			for (UserGroup user : userGroups) {
				currentGroups.add(user.getGroupId());
			}
		}
		return onSuccess(currentGroups);
	}

	@Override
	@Path("/vaidateOTP")
	@GET
	@Timed(name = "Add user roles#timer")
	public Response vaidateOTP(@QueryParam("userId") String userId,
			@QueryParam("email") String email, @QueryParam("otp") String otp,
			@Context HttpServletRequest request) throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		UserFilter filter = new UserFilter();
		if (StringUtils.isNotBlank(userId)) {
			filter.setId(userId);
		}
		if (StringUtils.isNotBlank(email)) {
			filter.setEmail(email);
		}
		LOGGER.info("vaidateOTP filter => " + filter);
		LOGGER.info("vaidateOTP requestTracker => " + requestTracker);
		User user = userManagement.findUser(requestTracker, filter);
		if (user == null) {
			return onError(new UserException("user.dosesnt.exists")
					.setCustomMessage("User doesn't exist!"));
		}
		return onSuccess(otp.equalsIgnoreCase(user.getOtp()));
	}

	@Override
	@Path("/generateOTP")
	@POST
	@Timed(name = "Generate otp#timer")
	public Response generateOTP(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = new AbstractRequestTracker(
					request);
			UserFilter userFilter = new ObjectMapper().readValue(data,
					UserFilter.class);
			if (StringUtils.isBlank(userFilter.getId())
					&& StringUtils.isBlank(userFilter.getEmail())) {
				return onError("bad.request", Arrays.asList("Bad request!"));
			}
			User user = userManagement.findUser(requestTracker, userFilter);
			if (user == null) {
				return onError(new UserException("user.dosesnt.exists")
						.setCustomMessage("User doesn't exist!"));
			}
			String type = null;
			if ("SIGNUP".equalsIgnoreCase(userFilter.getOtpPurpose())) {
				type = AbstractConfiguration.getProperty("user.signup.otp");
			} else if ("FORGOT_PASSWORD".equalsIgnoreCase(userFilter
					.getOtpPurpose())) {
				type = AbstractConfiguration.getProperty("forgot.password.otp");
			}
			return onSuccess(userManagement.generateUserOTP(requestTracker,
					user.getId(), type));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	@Path("/logout")
	@POST
	@Timed(name = "Logout#timer")
	public Response logout(String data, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		Device device;
		try {
			device = new ObjectMapper().readValue(data, Device.class);
		} catch (Exception e1) {
			return onError("bad.request", Arrays.asList("Bad request!"));
		}
		LOGGER.info("Logout user => " + device);

		userManagement.updateDeviceStatus(requestTracker, device);
		return onSuccess("successfully sign out!!");
	}

	@Override
	@Path("/{userId}/updateEmail")
	@PUT
	@Timed(name = "Logout#timer")
	public Response updateEmail(@PathParam("userId") String userId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {

		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);

		ObjectMapper mapper = new ObjectMapper();
		User user;
		try {
			user = mapper.readValue(data, User.class);
		} catch (IOException e) {
			throw new AbstractException(e);
		}

		try {
			userManagement.validateUserExists(requestTracker, user);
		} catch (UserException e) {
			return onError(e);
		}
		try {
			return onSuccess(userManagement.updateUserEmail(requestTracker,
					user, userId));
		} catch (UserException e) {
			return onError(e);
		}
	}

	@Override
	@Path("/addSchool")
	@POST
	public Response addSchool(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			LOGGER.info("data ==> " + data);
			School school = new ObjectMapper().readValue(data, School.class);

			School dbSchool = userManagement.checkSchoolExists(
					new AbstractRequestTracker(request), school);
			if (dbSchool != null) {
				LOGGER.info("School Already exists ==> " + dbSchool.toString());
				return onSuccess(dbSchool);
			}

			return onSuccess(userManagement.addNewSchool(
					new AbstractRequestTracker(request), school));

		} catch (IOException e) {
			e.printStackTrace();
			return onError(new AbstractException("oops")
					.setCustomMessage("Something went wrong!"));
		} catch (AbstractException e) {
			return onError(e);
		}
	}

	@Override
	@Path("/listActiveSchools")
	@GET
	@Timed(name = "List Active schools #timer")
	public Response listActiveSchools(@QueryParam("status") String status,
			@Context HttpServletRequest request) throws AbstractException {

		SchoolFilter filter = new SchoolFilter();
		if (StringUtils.isNotBlank(status)) {
			if (!status.equalsIgnoreCase("all"))
				filter.setStatus(Integer.valueOf(status));
		} else {
			filter.setStatus(1);
		}

		SortRules sortRules = new SortRules();
		sortRules.add(new SortRule(SchoolMapper.SCHOOL_NAME, SortOrder.ASC));
		try {
			return onSuccess(userManagement.listofActiveSchools(
					new AbstractRequestTracker(request), filter, new Paging(-1,
							-1), sortRules));
		} catch (AbstractException e) {
			return onError(e);
		}
	}

	@Override
	@Path("/school/{schoolId}")
	@GET
	@Timed(name = "Get school data by Id #timer")
	public Response findSchoolById(@PathParam("schoolId") String schoolId,
			@QueryParam("schoolPod") String schoolPod,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(userManagement.findSchoolById(
				new AbstractRequestTracker(request), schoolId, schoolPod));
	}

	@Override
	@Path("/uploadPodLogo")
	@POST
	@Timed(name = "Upload school Pod logo#timer")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadPodLogo(
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@QueryParam("podId") String id, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		// Get the file extension
		String fileToUpload = fileDetail.getFileName();
		String extension = fileToUpload.substring(
				fileToUpload.lastIndexOf("."), fileToUpload.length());
		String fileName;
		try {
			fileName = s3Uploader.upload(inputStream, SCHOOL_POD_LOGO, id,
					extension, true);
		} catch (IOException e) {
			throw new AbstractException(e);
		}

		if (userManagement.updateSchoolPodLogo(requestTracker, id, fileName))
			return onSuccess(fileName);
		else
			return onError(new AbstractException("upload.pod.logo")
					.setCustomMessage("oops!Something went wrong.Please try again!"));
	}

	@Override
	@Path("/{schoolId}/updateSchool")
	@PUT
	@Timed(name = "Update school or POD related data#timer")
	public Response updateSchool(@PathParam("schoolId") String schoolId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {

		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);

		ObjectMapper mapper = new ObjectMapper();
		School school;
		SchoolFilter filter = new SchoolFilter();
		try {
			school = mapper.readValue(data, School.class);
		} catch (IOException e) {
			throw new AbstractException(e);
		}

		try {
			userManagement.findSchoolById(requestTracker, schoolId, "");
		} catch (AbstractException e) {
			return onError(e);
		}

		filter.setId(schoolId);

		try {
			return onSuccess(userManagement.updateSchool(requestTracker,
					school, filter));
		} catch (UserException e) {
			return onError(e);
		}
	}
}

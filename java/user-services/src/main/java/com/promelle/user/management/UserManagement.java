package com.promelle.user.management;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
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

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface UserManagement {

	User findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	User addUser(AbstractRequestTracker requestTracker, User user)
			throws AbstractException;

	User activateUser(AbstractRequestTracker requestTracker, UserFilter filter)
			throws AbstractException;

	User authenticate(AbstractRequestTracker requestTracker, User user)
			throws UserException, AbstractException;

	boolean update(AbstractRequestTracker requestTracker, User user,
			UserFilter filter) throws AbstractException;

	List<Role> getUserRoles(AbstractRequestTracker requestTracker, String userId)
			throws AbstractException;

	List<Permission> getUserPermissions(AbstractRequestTracker requestTracker,
			String userId) throws AbstractException;

	List<Group> getUserGroups(AbstractRequestTracker requestTracker,
			String userId) throws AbstractException;

	List<Role> getUserNewRoles(AbstractRequestTracker requestTracker,
			String userId, String accountId) throws AbstractException;

	List<Permission> getUserNewPermissions(
			AbstractRequestTracker requestTracker, String userId,
			String accountId) throws AbstractException;

	List<Group> getUserNewGroups(AbstractRequestTracker requestTracker,
			String userId, String accountId) throws AbstractException;

	List<UserRole> addUserRoles(AbstractRequestTracker requestTracker,
			String userId, List<String> roleIds) throws AbstractException;

	List<UserPermission> addUserPermissions(
			AbstractRequestTracker requestTracker, String userId,
			List<String> permissionIds) throws AbstractException;

	List<UserGroup> addUserGroups(AbstractRequestTracker requestTracker,
			String userId, List<String> groupIds) throws AbstractException;

	UserRole deleteUserRole(AbstractRequestTracker requestTracker,
			String userId, String roleId) throws AbstractException;

	UserPermission deleteUserPermission(AbstractRequestTracker requestTracker,
			String userId, String permissionId) throws AbstractException;

	UserGroup deleteUserGroup(AbstractRequestTracker requestTracker,
			String userId, String groupId) throws AbstractException;

	List<Permission> getUserFinalPermissions(
			AbstractRequestTracker requestTracker, String userId)
			throws AbstractException;

	void validateUserExists(AbstractRequestTracker requestTracker, User user)
			throws UserException, AbstractException;

	PagedList<User> listUsers(AbstractRequestTracker requestTracker,
			UserFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException;

	User findUser(AbstractRequestTracker requestTracker, UserFilter filter)
			throws AbstractException;

	boolean updateProfilePic(AbstractRequestTracker requestTracker, String id,
			String fileName) throws AbstractException;

	int deleteUser(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	boolean generateUserOTP(AbstractRequestTracker requestTracker,
			String userId, String type) throws AbstractException;

	/**
	 * Save device information linked to registered user
	 * 
	 * @param requestTracker
	 * @param user
	 */
	void addDevice(AbstractRequestTracker requestTracker, Device device)
			throws AbstractException;

	void updateDevice(AbstractRequestTracker requestTracker, Device device)
			throws AbstractException;

	void updateDeviceStatus(AbstractRequestTracker requestTracker, Device device)
			throws AbstractException;

	Device checkDeviceExists(AbstractRequestTracker requestTracker,
			Device device) throws AbstractException;

	int updateUserEmail(AbstractRequestTracker requestTracker, User user,
			String id) throws AbstractException;

	School addNewSchool(AbstractRequestTracker requestTracker, School school)
			throws AbstractException;

	School checkSchoolExists(AbstractRequestTracker requestTracker,
			School school) throws AbstractException;

	PagedList<School> listofActiveSchools(
			AbstractRequestTracker requestTracker, SchoolFilter schoolFilter,
			Paging paging, SortRules sortRules) throws AbstractException;

	void populateUsersList(AbstractRequestTracker requestTracker,
			PagedList<User> usersList) throws AbstractException;

	User populateUser(AbstractRequestTracker requestTracker, User user)
			throws AbstractException;

	School findSchoolById(AbstractRequestTracker requestTracker,
			String schoolId, String schoolPod) throws AbstractException;

	/**
	 * Save user validation logic change
	 * 
	 * @param requestTracker
	 * @param user
	 * @throws UserException
	 * @throws AbstractException
	 */
	void validateAddUserExists(AbstractRequestTracker requestTracker, User user)
			throws UserException, AbstractException;

	// School Pod related changes or services
	/*
	 * Update school pod logo
	 */
	boolean updateSchoolPodLogo(AbstractRequestTracker requestTracker,
			String id, String fileName) throws AbstractException;

	int updateSchool(AbstractRequestTracker requestTracker, School school,
			SchoolFilter filter) throws AbstractException;
}

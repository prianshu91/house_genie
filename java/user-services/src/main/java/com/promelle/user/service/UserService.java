package com.promelle.user.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface UserService {

	/**
	 * Get the list of all roles for provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserRoles(String userId, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Authenticate user.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param email
	 * @param password
	 * @return response
	 * @throws AbstractException
	 */
	Response login(String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Activate user.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	Response activate(String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Get the list of all permissions for provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserPermissions(String userId, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Get the list of all groups for provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserGroups(String userId, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Add new roles to the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	Response addUserRoles(String userId, String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Add new permissions to the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	Response addUserPermissions(String userId, String data,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Add new groups to the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	Response addUserGroups(String userId, String data,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Remove a role from the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param roleId
	 * @return response
	 * @throws AbstractException
	 */
	Response deleteUserRole(String userId, String roleId,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Remove a permission from the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param permissionId
	 * @return response
	 * @throws AbstractException
	 */
	Response deleteUserPermission(String userId, String permissionId,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Remove a group from the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param groupId
	 * @return response
	 * @throws AbstractException
	 */
	Response deleteUserGroup(String userId, String groupId,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Get the list of all roles that are not added in the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param accountId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserNewRoles(String userId, String accountId,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Get the list of all permissions that are not added in the provided user
	 * id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param accountId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserNewPermissions(String userId, String accountId,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Get the list of all groups that are not added in the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @param accountId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserNewGroups(String userId, String accountId,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Get the list of final permissions for the provided user id.
	 * 
	 * @param requestId
	 * @param portalName
	 * @param userId
	 * @return response
	 * @throws AbstractException
	 */
	Response getUserFinalPermissions(String userId, HttpServletRequest request)
			throws AbstractException;

	Response forgetPassword(String data, HttpServletRequest request)
			throws AbstractException;

	Response resetPassword(String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Uploads profile picture
	 * 
	 * @param userId
	 * @return response
	 * @throws AbstractException
	 */
	Response uploadPicture(InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail, String id,
			HttpServletRequest request) throws AbstractException;

	Response enableUser(String id, HttpServletRequest request)
			throws AbstractException;

	Response disableUser(String id, HttpServletRequest request)
			throws AbstractException;

	Response updateUserPermissions(String userId, String data,
			HttpServletRequest request) throws AbstractException;

	Response updateUserRoles(String userId, String data,
			HttpServletRequest request) throws AbstractException;

	Response updateUserGroups(String userId, String data,
			HttpServletRequest request) throws AbstractException;

	Response vaidateOTP(String userId, String email, String otp,
			HttpServletRequest request) throws AbstractException;

	Response generateOTP(String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Authenticate user and successfully update required information .
	 * 
	 * @param data
	 * @param request
	 * @return response
	 * @throws AbstractException
	 */
	Response logout(String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Update email for existing user
	 * 
	 * @param userId
	 * @param user
	 * @param request
	 * @return
	 * @throws AbstractException
	 */
	Response updateEmail(String userId, String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Add a new school information
	 * 
	 * @param data
	 * @param request
	 * @return
	 * @throws AbstractException
	 */
	Response addSchool(String data, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Return list of all active schools for app
	 * 
	 * @param request
	 * @return
	 * @throws AbstractException
	 */
	Response listActiveSchools(String status, HttpServletRequest request)
			throws AbstractException;

	/**
	 * Find a school's data by schoolId
	 * 
	 * @param schoolId
	 * @param request
	 * @return
	 * @throws AbstractException
	 */
	Response findSchoolById(String schoolId, String schoolPod,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Upload School POD logo picture
	 * 
	 * @param inputStream
	 * @param fileDetail
	 * @param id
	 * @param request
	 * @return
	 * @throws AbstractException
	 */
	Response uploadPodLogo(InputStream inputStream,
			FormDataContentDisposition fileDetail, String id,
			HttpServletRequest request) throws AbstractException;

	/**
	 * Update schools data
	 * 
	 * @param schoolId
	 * @param data
	 * @param request
	 * @return
	 * @throws AbstractException
	 */
	Response updateSchool(String schoolId, String data,
			HttpServletRequest request) throws AbstractException;
}

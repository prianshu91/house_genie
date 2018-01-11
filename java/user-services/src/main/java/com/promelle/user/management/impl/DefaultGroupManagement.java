package com.promelle.user.management.impl;

import java.util.ArrayList;
import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.user.dao.GroupDao;
import com.promelle.user.dao.GroupRoleDao;
import com.promelle.user.dao.RoleDao;
import com.promelle.user.dto.Group;
import com.promelle.user.dto.GroupRole;
import com.promelle.user.dto.Role;
import com.promelle.user.management.GroupManagement;
import com.promelle.utils.UUIDUtils;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultGroupManagement implements GroupManagement {

	private RoleDao roleDao;
	private GroupDao groupDao;
	private GroupRoleDao groupRoleDao;

	public DefaultGroupManagement(RoleDao roleDao, GroupDao groupDao,
			GroupRoleDao groupRoleDao) {
		super();
		this.roleDao = roleDao;
		this.groupDao = groupDao;
		this.groupRoleDao = groupRoleDao;
	}

	@Override
	public String addGroup(AbstractRequestTracker requestTracker, Group group)
			throws AbstractException {
		groupDao.save(requestTracker, group);
		return group.getId();
	}

	@Override
	public List<Role> getGroupRoles(AbstractRequestTracker requestTracker,
			String groupId) throws AbstractException {
		List<String> roleIds = groupRoleDao.getRoleIdsByGroupId(groupId);
		return roleIds == null || roleIds.isEmpty() ? new ArrayList<>()
				: roleDao.findByIds(roleIds);
	}

	@Override
	public List<Role> getGroupNewRoles(AbstractRequestTracker requestTracker,
			String groupId) {
		List<String> roleIds = groupRoleDao.getRoleIdsByGroupId(groupId);
		return roleIds == null || roleIds.isEmpty() ? roleDao.findAll()
				: roleDao.findAllMinusIds(roleIds);
	}

	@Override
	public List<GroupRole> addGroupRoles(AbstractRequestTracker requestTracker,
			String groupId, List<String> roleIds) {
		List<GroupRole> groupRoles = new ArrayList<>();
		for (String roleId : roleIds) {
			GroupRole groupRole = new GroupRole();
			groupRole.setId(UUIDUtils.getUUID());
			groupRole.setGroupId(groupId);
			groupRole.setRoleId(roleId);
			groupRoleDao.save(requestTracker, groupRole);
			groupRoles.add(groupRole);
		}
		return groupRoles;
	}

	@Override
	public boolean deleteGroup(AbstractRequestTracker requestTracker,
			String groupId) {
		return groupDao.softDelete(requestTracker, groupId) > 0;
	}

	@Override
	public boolean deleteGroupRole(AbstractRequestTracker requestTracker,
			String groupId, String roleId) {
		return groupRoleDao.deleteGroupRole(groupId, roleId) > 0;
	}

	@Override
	public List<Group> getAccountGroups(AbstractRequestTracker requestTracker,
			String accountId) {
		return groupDao.findByAccountId(accountId);
	}

}

package com.promelle.user.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.user.dto.Group;
import com.promelle.user.dto.GroupRole;
import com.promelle.user.management.GroupManagement;
import com.promelle.user.service.GroupService;
import com.promelle.utils.UUIDUtils;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/group")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultGroupService extends AbstractService<Group, SearchFilter>
		implements GroupService {

	private GroupManagement groupManagement;
	private MessageProducer messageProducer;

	private static final String GROUP_CREATED = "group.created";
	private static final String GROUP_ROLE_ADDED = "group.role.added";
	private static final String GROUP_ROLE_DELETED = "group.role.deleted";

	public DefaultGroupService(GroupManagement groupManagement,
			MessageProducer messageProducer) {
		super(Group.class, SearchFilter.class);
		this.groupManagement = groupManagement;
		this.messageProducer = messageProducer;
	}

	@Override
	@Timed(name = "Add group#timer")
	public Response save(AbstractRequestTracker requestTracker, Group group)
			throws AbstractException {
		if (StringUtils.isBlank(group.getId())) {
			group.setId(UUIDUtils.getUUID());
		}
		if (StringUtils.isBlank(group.getCode())) {
			group.setCode(group.getName().toUpperCase().replace(" ", "_"));
		}

		TopicMessage msg = new TopicMessage(requestTracker,
				AbstractConfiguration.getProperty(GROUP_CREATED));
		msg.setData(group.toString());
		messageProducer.sendMessage(msg);

		return onSuccess(groupManagement.addGroup(requestTracker, group));
	}

	@Override
	public Response update(AbstractRequestTracker requestTracker, Group group,
			SearchFilter filter) throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		return onSuccess(groupManagement.deleteGroup(requestTracker, id));
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response list(AbstractRequestTracker requestTracker,
			SearchFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Path("/{groupId}/roles")
	@GET
	@Timed(name = "Get group roles#timer")
	public Response getGroupRoles(@PathParam("groupId") String groupId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(groupManagement.getGroupRoles(
				new AbstractRequestTracker(request), groupId));
	}

	@Override
	@Path("/{groupId}/roles/new")
	@GET
	@Timed(name = "Get group new roles#timer")
	public Response getGroupNewRoles(@PathParam("groupId") String groupId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(groupManagement.getGroupNewRoles(
				new AbstractRequestTracker(request), groupId));
	}

	@Override
	@Path("/{groupId}/roles")
	@POST
	@Timed(name = "Add group roles#timer")
	public Response addGroupRoles(@PathParam("groupId") String groupId,
			String data, @Context HttpServletRequest request)
			throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		List<GroupRole> groupRoles;
		try {
			groupRoles = groupManagement.addGroupRoles(
					requestTracker,
					groupId,
					Arrays.asList(new ObjectMapper().readTree(data)
							.get("roleIds").asText().split(",")));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		if (!groupRoles.isEmpty()) {
			TopicMessage msg = new TopicMessage(requestTracker,
					AbstractConfiguration.getProperty(GROUP_ROLE_ADDED));
			msg.setData(groupRoles.toString());
			messageProducer.sendMessage(msg);
		}
		return onSuccess(groupRoles);
	}

	@Override
	@Path("/{groupId}/role/{roleId}")
	@DELETE
	@Timed(name = "Delete group roles#timer")
	public Response deleteGroupRole(@PathParam("groupId") String groupId,
			@PathParam("roleId") String roleId,
			@Context HttpServletRequest request) throws AbstractException {
		if (groupManagement.deleteGroupRole(
				new AbstractRequestTracker(request), groupId, roleId)) {
			GroupRole groupRole = new GroupRole();
			groupRole.setGroupId(groupId);
			groupRole.setRoleId(roleId);

			TopicMessage msg = new TopicMessage(new AbstractRequestTracker(
					request),
					AbstractConfiguration.getProperty(GROUP_ROLE_DELETED));
			msg.setData(groupRole.toString());
			messageProducer.sendMessage(msg);
		}
		return onSuccess(roleId);
	}

	@Override
	@Path("/account/{accountId}")
	@GET
	@Timed(name = "Get account groups#timer")
	public Response getAccountGroups(@PathParam("accountId") String accountId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(groupManagement.getAccountGroups(
				new AbstractRequestTracker(request), accountId));
	}

}

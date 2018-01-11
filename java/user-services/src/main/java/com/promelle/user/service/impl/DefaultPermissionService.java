package com.promelle.user.service.impl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;
import com.promelle.user.dto.Permission;
import com.promelle.user.management.PermissionManagement;
import com.promelle.user.service.PermissionService;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/permission")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultPermissionService extends
		AbstractService<Permission, SearchFilter> implements PermissionService {

	private PermissionManagement permissionManagement;

	public DefaultPermissionService(PermissionManagement permissionManagement) {
		super(Permission.class, SearchFilter.class);
		this.permissionManagement = permissionManagement;
	}

	@Override
	protected Response save(AbstractRequestTracker requestTracker,
			Permission obj) throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Response update(AbstractRequestTracker requestTracker,
			Permission obj, SearchFilter filter) throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Timed(name = "Get all permissions#timer")
	public Response list(AbstractRequestTracker requestTracker,
			SearchFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return onSuccess(permissionManagement.list(requestTracker, paging,
				sortRules));
	}

}

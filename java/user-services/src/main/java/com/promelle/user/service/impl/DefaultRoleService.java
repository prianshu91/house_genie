package com.promelle.user.service.impl;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;
import com.promelle.user.dto.Role;
import com.promelle.user.management.RoleManagement;
import com.promelle.user.service.RoleService;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultRoleService extends AbstractService<Role, SearchFilter> implements RoleService {

    private RoleManagement roleManagement;

    public DefaultRoleService(RoleManagement roleManagement) {
        super(Role.class, SearchFilter.class);
        this.roleManagement = roleManagement;
    }

    @Override
    @Path("/{roleId}/permissions")
    @GET
    @Timed(name = "Get role permissions#timer")
    public Response getRolePermissions(@PathParam("roleId") String roleId, @Context HttpServletRequest request)
            throws AbstractException {
        return onSuccess(roleManagement.getRolePermissions(new AbstractRequestTracker(request), Arrays.asList(roleId)));
    }

    @Override
    protected Response save(AbstractRequestTracker requestTracker, Role obj) throws AbstractException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Response update(AbstractRequestTracker requestTracker, Role obj, SearchFilter filter) throws AbstractException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response deleteById(AbstractRequestTracker requestTracker, String id) throws AbstractException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response findById(AbstractRequestTracker requestTracker, String id) throws AbstractException {
        throw new UnsupportedOperationException();
    }

    @Override
    @Timed(name = "Get all roles#timer")
    protected Response list(AbstractRequestTracker requestTracker, SearchFilter filter, Paging paging,
            SortRules sortRules) throws AbstractException {
        return onSuccess(roleManagement.list(requestTracker, paging, sortRules));
    }

}

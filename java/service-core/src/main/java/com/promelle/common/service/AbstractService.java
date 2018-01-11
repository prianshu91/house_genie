package com.promelle.common.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.dto.AbstractDTO;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;

/**
 * 
 * This class is intended for providing base services.
 * 
 * @author Hemant Kumar
 * @version 1.0
 * 
 * @param <T>
 *            extends {@link AbstractDTO}
 * @param <E>
 *            extends {@link SearchFilter}
 */
public abstract class AbstractService<T extends AbstractDTO, E extends SearchFilter> extends BaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractService.class.getName());
	private Class<T> dtoType;
	private Class<E> filterType;

	public AbstractService(Class<T> dtoType, Class<E> filterType) {
		super();
		this.dtoType = dtoType;
		this.filterType = filterType;
	}

	/**
	 * Add new record
	 * 
	 * @param obj
	 * @return response
	 * @throws Exception
	 */
	protected abstract Response save(AbstractRequestTracker requestTracker,
			T dto) throws AbstractException;

	/**
	 * Update record
	 * 
	 * @param id
	 * @param dto
	 * @param filter
	 * @return response
	 * @throws Exception
	 */
	protected abstract Response update(AbstractRequestTracker requestTracker,
			T dto, E filter) throws AbstractException;

	/**
	 * List records
	 * 
	 * @param requestTracker
	 * @param filter
	 * @param paging
	 * @return response
	 * @throws AbstractException
	 */
	protected abstract Response list(AbstractRequestTracker requestTracker,
			E filter, Paging paging, SortRules sortRules)
			throws AbstractException;

	/**
	 * Find by id
	 * 
	 * @param requestTracker
	 * @param id
	 * @return response
	 * @throws AbstractException
	 */
	protected abstract Response findById(AbstractRequestTracker requestTracker,
			String id) throws AbstractException;

	/**
	 * Delete by id
	 * 
	 * @param requestTracker
	 * @param id
	 * @return response
	 * @throws AbstractException
	 */
	protected abstract Response deleteById(
			AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	/**
	 * Add new record
	 * 
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	@POST
	public Response save(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			return save(new AbstractRequestTracker(request),
					new ObjectMapper().readValue(data, dtoType));
		} catch (IOException e) {
			throw new AbstractException("save.error", e);
		}
	}

	/**
	 * Update record
	 * 
	 * @param id
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") String id, String data,
			@Context HttpServletRequest request) throws AbstractException {
		try {
			E filter = filterType.newInstance();
			filter.setId(id);
			AbstractRequestTracker requestTracker = new AbstractRequestTracker(
					request);
			T dto = new ObjectMapper().readValue(data, dtoType);
			if (dto instanceof AbstractAuditDTO) {
				AbstractAuditDTO b = (AbstractAuditDTO) dto;
				b.setModifiedOn(new Date().getTime());
				b.setModifiedBy(requestTracker.getRequestBy());
			}
			System.out.println("filter => " + filter);
			System.out.println("dto => " + dto);
			return update(requestTracker, dto, filter);
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			throw new AbstractException("update.error", e);
		}
	}

	/**
	 * List records
	 * 
	 * @param request
	 * @return response
	 * @throws AbstractException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("/list")
	public Response list(@Context HttpServletRequest request)
			throws AbstractException {
		Map<String, Object> map = new HashMap<>();
		Enumeration<String> params = request.getParameterNames();
		SortRules sortRules = null;
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			if ("sort".equalsIgnoreCase(param)) {
				sortRules = SortRules.parse(request.getParameter(param));
				continue;
			}
			JSONArray array = null;
			try {
				array = new JSONArray(request.getParameter(param));
			} catch (JSONException e) {
				LOGGER.trace("Error trying to parse to array", e);
			}
			if (map.get(param) != null) {
				List list = map.get(param) instanceof List ? (List) map
						.get(param) : new ArrayList();
				list.add(map.get(param));
				map.put(param, list);
			} else if (array != null) {
				map.put(param, processJsonArray(array));
			} else {
				map.put(param, request.getParameter(param));
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		return list(new AbstractRequestTracker(request),
				(E) mapper.convertValue(map, filterType),
				mapper.convertValue(map, Paging.class), sortRules);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List processJsonArray(JSONArray array) throws AbstractException {
		List list = new ArrayList();
		for (int i = 0; i < array.length(); i++) {
			try {
				list.add(array.get(i));
			} catch (JSONException e) {
				throw new AbstractException("list.error", e);
			}
		}
		return list;
	}

	@GET
	@Path("/{id}")
	public Response find(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		return findById(new AbstractRequestTracker(request), id);
	}

	@DELETE
	@Path("/{id}")
	public Response deleteById(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		return deleteById(new AbstractRequestTracker(request), id);
	}

	public Class<T> getDtoType() {
		return dtoType;
	}

	public Class<E> getFilterType() {
		return filterType;
	}

}

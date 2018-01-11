package com.promelle.common.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.constants.Punctuation;
import com.promelle.dto.AbstractDTO;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.utils.ListUtils;

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
public abstract class AbstractBulkService<T extends AbstractDTO, E extends SearchFilter>
		extends AbstractService<T, E> {

	public AbstractBulkService(Class<T> dtoType, Class<E> filterType) {
		super(dtoType, filterType);
	}

	/**
	 * Add new record
	 * 
	 * @param obj
	 * @return response
	 * @throws Exception
	 */
	protected abstract Response bulkSave(AbstractRequestTracker requestTracker,
			List<T> dtos) throws AbstractException;

	/**
	 * Update record
	 * 
	 * @param id
	 * @param dto
	 * @param filter
	 * @return response
	 * @throws Exception
	 */
	protected abstract Response bulkUpdate(
			AbstractRequestTracker requestTracker, List<T> dtos, E filter)
			throws AbstractException;

	/**
	 * delete record
	 * 
	 * @param id
	 * @param dto
	 * @param filter
	 * @return response
	 * @throws Exception
	 */
	protected abstract Response bulkDelete(
			AbstractRequestTracker requestTracker, List<String> ids)
			throws AbstractException;

	@DELETE
	@Path("/bulk/{ids}")
	public Response bulkDelete(@PathParam("ids") String ids,
			@Context HttpServletRequest request) throws AbstractException {
		return bulkDelete(
				new AbstractRequestTracker(request),
				ListUtils.splitStringIntoTrimmedList(ids,
						Punctuation.COMMA.toString()));
	}

	/**
	 * Add new record
	 * 
	 * @param data
	 * @return response
	 * @throws AbstractException
	 */
	@POST
	@Path("/bulk")
	public Response bulkSave(String data, @Context HttpServletRequest request)
			throws AbstractException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return bulkSave(new AbstractRequestTracker(request),
					mapper.readValue(data, mapper.getTypeFactory()
							.constructCollectionType(List.class, getDtoType())));
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
	@Path("/bulk")
	public Response bulkUpdate(String data, @Context HttpServletRequest request)
			throws AbstractException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// TODO: check for filter
			return bulkUpdate(
					new AbstractRequestTracker(request),
					mapper.readValue(data, mapper.getTypeFactory()
							.constructCollectionType(List.class, getDtoType())),
					null);

		} catch (IllegalArgumentException | IOException e) {
			throw new AbstractException("update.error", e);
		}
	}

}

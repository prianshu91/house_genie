package com.promelle.product.service.impl;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.ProductMessage;
import com.promelle.product.filter.ProductMessageFilter;
import com.promelle.product.management.ProductManagement;
import com.promelle.product.service.MessageService;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.CommonResponse;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/product/message")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultMessageService extends
		AbstractService<ProductMessage, ProductMessageFilter> implements
		MessageService {
	private ProductManagement productManagement;

	public DefaultMessageService(ProductManagement productManagement) {
		super(ProductMessage.class, ProductMessageFilter.class);
		this.productManagement = productManagement;
	}

	@Override
	@Timed(name = "Add textMessage#timer")
	protected Response save(AbstractRequestTracker requestTracker,
			ProductMessage textMessage) throws AbstractException {
		textMessage.setTimestamp(Calendar.getInstance().getTimeInMillis());
		textMessage.setConversationId(productManagement.getConversationId(
				requestTracker, textMessage.getSenderId(),
				textMessage.getReceiverId(), textMessage.getProductId()));
		Product product = productManagement.findProductById(requestTracker,
				textMessage.getProductId());
		textMessage.setProductTitle(product.getTitle());
		textMessage.setNew(true);
		productManagement.sendMessage(requestTracker, textMessage);
		return onSuccess(textMessage);
	}

	@Override
	@Timed(name = "Update textMessage#timer")
	protected Response update(AbstractRequestTracker requestTracker,
			ProductMessage textMessage, ProductMessageFilter filter)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Timed(name = "Delete textMessage#timer")
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
	@Timed(name = "List textMessages#timer")
	protected Response list(AbstractRequestTracker requestTracker,
			ProductMessageFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		PagedList<ProductMessage> pagedList = productManagement.listMessages(
				requestTracker, filter, paging, sortRules);
		int unreadCount = 0;
		for (ProductMessage message : pagedList.getObjects()) {
			unreadCount += message.getUnreadCount() != null ? message
					.getUnreadCount() : 0;
		}
		return Response.ok(
				new CommonResponse().setData(pagedList.getObjects())
						.setPaging(pagedList.getPagination())
						.addAdditionalData("unreadCount", unreadCount)
						.toString()).build();
	}

	@Path("/{messageId}/user/{userId}")
	@DELETE
	@Override
	public Response deleteMessage(@PathParam("messageId") String messageId,
			@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(productManagement.deleteMessage(
				new AbstractRequestTracker(request), messageId, userId));
	}

	@Path("/conversation/{conversationId}/user/{userId}/read")
	@PUT
	@Override
	public Response readConversation(
			@PathParam("conversationId") String conversationId,
			@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(productManagement.readConveration(
				new AbstractRequestTracker(request), conversationId, userId));
	}

	@Path("/conversation/{conversationId}/user/{userId}")
	@DELETE
	@Override
	public Response deleteConversation(
			@PathParam("conversationId") String conversationId,
			@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		productManagement.deleteConveration(
				new AbstractRequestTracker(request), conversationId, userId);
		return onSuccess(conversationId);
	}
}

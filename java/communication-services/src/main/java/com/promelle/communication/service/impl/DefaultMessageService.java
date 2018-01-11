package com.promelle.communication.service.impl;

import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.promelle.common.service.AbstractService;
import com.promelle.communication.dto.Message;
import com.promelle.communication.filter.MessageFilter;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.communication.service.MessageService;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/communication/message")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultMessageService extends
		AbstractService<Message, MessageFilter> implements
		MessageService {
	private CommunicationManagement messageManagement;

	public DefaultMessageService(CommunicationManagement messageManagement) {
		super(Message.class, MessageFilter.class);
		this.messageManagement = messageManagement;
	}

	@Override
	@Timed(name = "Add textMessage#timer")
	protected Response save(AbstractRequestTracker requestTracker,
			Message textMessage) throws AbstractException {
		textMessage.setTimestamp(Calendar.getInstance().getTimeInMillis());
		messageManagement.sendMessage(requestTracker, textMessage);
		return onSuccess(textMessage);
	}

	@Override
	@Timed(name = "Update textMessage#timer")
	protected Response update(AbstractRequestTracker requestTracker,
			Message textMessage, MessageFilter filter)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Timed(name = "Delete textMessage#timer")
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		messageManagement.deleteMessage(requestTracker, id);
		return onSuccess(id);
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Timed(name = "List textMessages#timer")
	protected Response list(AbstractRequestTracker requestTracker,
			MessageFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return onSuccess(messageManagement.listMessages(requestTracker, filter,
				paging, sortRules));
	}

	@Path("/users")
	@GET
	@Override
	public Response getUsers() throws AbstractException {
		return onSuccess(messageManagement.getUsers());
	}

}

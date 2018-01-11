package com.promelle.communication.service.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.service.BaseService;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.communication.sender.EmailSender;
import com.promelle.communication.service.EmailService;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.utils.JsonUtils;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/communication/email")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultEmailService extends BaseService implements EmailService {
	private EmailSender emailSender;
	private CommunicationManagement communicationManagement;

	public DefaultEmailService(EmailSender emailSender,
			CommunicationManagement communicationManagement) {
		this.emailSender = emailSender;
		this.communicationManagement = communicationManagement;
	}

	@POST
	@Override
	public Response sendEmail(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			JsonNode node = new ObjectMapper().readTree(data);
			String to = JsonUtils.getStringValue(node, "to", null);
			if (!to.contains("@")) {
				to = communicationManagement.findUserById(
						new AbstractRequestTracker(request), to).getEmail();
			}
			return sendEmail(node, to);
		} catch (Exception e) {
			return onError(new AbstractException("bad.request"));
		}
	}

	@POST
	@Override
	@Path("/admin")
	public Response sendEmailToAdmin(String data,
			@Context HttpServletRequest request) throws AbstractException {
		try {
			return sendEmail(new ObjectMapper().readTree(data),
					"contact@promelle.com");
		} catch (Exception e) {
			return onError(new AbstractException("bad.request"));
		}
	}

	private Response sendEmail(JsonNode node, String to)
			throws AbstractException {
		String from = JsonUtils.getStringValue(node, "from", null);
		String subject = JsonUtils.getStringValue(node, "subject", null);
		String body = JsonUtils.getStringValue(node, "body", null);
		String bcc = JsonUtils.getStringValue(node, "bcc", null);

		return onSuccess(emailSender.send(to, from, bcc, subject, body));
	}

	@POST
	@Override
	@Path("/broadcast")
	public Response broadcastEmail(String data,
			@Context HttpServletRequest request) throws AbstractException {

		JsonNode node;
		try {
			node = new ObjectMapper().readTree(data);
		} catch (IOException e) {
			e.printStackTrace();
			return onError(new AbstractException("json.parsing.error")
					.setCustomMessage(e.getMessage()));
		}

		String from = JsonUtils.getStringValue(node, "from",
				"contact@promelle.com");
		String bcc = JsonUtils.getStringValue(node, "bcc", null);
		String subject = JsonUtils.getStringValue(node, "subject", null);
		String body = JsonUtils.getStringValue(node, "body", null);

		try {
			return onSuccess(communicationManagement.broadcastEmail(from, bcc,
					subject, body, new AbstractRequestTracker(request)));
		} catch (AbstractException e) {
			return onError(e);
		}
	}

}

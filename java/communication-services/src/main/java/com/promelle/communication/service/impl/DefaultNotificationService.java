package com.promelle.communication.service.impl;

import java.util.List;

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
import com.promelle.communication.filter.DeviceFilter;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.communication.sender.NotificationSender;
import com.promelle.communication.service.NotificationService;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.utils.JsonUtils;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
@Path("/communication/notification")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultNotificationService extends BaseService implements
		NotificationService {
	private NotificationSender notificationSender;
	private CommunicationManagement communicationManagement;

	public DefaultNotificationService(NotificationSender notificationSender,
			CommunicationManagement communicationManagement) {
		this.notificationSender = notificationSender;
		this.communicationManagement = communicationManagement;
	}

	@POST
	@Override
	public Response pushNotification(String data,
			@Context HttpServletRequest request) throws AbstractException {
		try {
			JsonNode node = new ObjectMapper().readTree(data);
			String userId = JsonUtils.getStringValue(node, "userId", null);
			String notificationMessage = JsonUtils.getStringValue(node,
					"message", null);
			String environment = JsonUtils.getStringValue(node, "env", null);

			List<String> devicesList = null;

			if (userId != null && userId.length() > 0) {
				DeviceFilter deviceFilter = new DeviceFilter();
				deviceFilter.setUserId(userId);
				deviceFilter.setStatus(1);

				devicesList = communicationManagement.getDevices(
						new AbstractRequestTracker(request), deviceFilter);
			}

			return onSuccess(notificationSender.push(devicesList,
					notificationMessage, environment));

		} catch (Exception e) {
			return onError(new AbstractException("bad.request"));
		}
	}
}

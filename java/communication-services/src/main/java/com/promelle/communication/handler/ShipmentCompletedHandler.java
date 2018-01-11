package com.promelle.communication.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.dto.User;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class ShipmentCompletedHandler extends CommunicationMessageHandler {

	private static final String SHIPMENT_COMPLETED_LENDER = "shipment-completed-lender.html";
	private static final String SHIPMENT_COMPLETED_RENTER = "shipment-completed-renter.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("shipment.completed"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = message.getRequestTracker();
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);

			User owner = getMessageManagement().findUserById(requestTracker,
					(String) map.get("ownerId"));
			User user = getMessageManagement().findUserById(requestTracker,
					(String) map.get("userId"));

			// Only last 7 chars of Shipment/Order id
			map.put("shipId", handleObjectLastChars((String) map.get("id"), 7));
			map.put("orderIdValue",
					handleObjectLastChars((String) map.get("orderId"), 7));

			getEmailSender().send(
					owner.getEmail(),
					"contact@promelle.com",
					"Congratulations! Order completed, Redeem earnin​gs",
					prepareMessage(readFile(SHIPMENT_COMPLETED_LENDER)
							.toString(), map));

			getEmailSender().send(
					user.getEmail(),
					"contact@promelle.com",
					"Thanks for renting!",
					prepareMessage(readFile(SHIPMENT_COMPLETED_RENTER)
							.toString(), map));

		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	private String handleObjectLastChars(String data, int count) {
		return data.length() > count ? data.substring(data.length() - count)
				: data;
	}
}

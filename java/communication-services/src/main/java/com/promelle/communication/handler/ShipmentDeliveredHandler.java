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
public class ShipmentDeliveredHandler extends CommunicationMessageHandler {

	private static final String SHIPMENT_DELIVERED = "shipment-delivered.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("shipment.delivered"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = message.getRequestTracker();
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);

			User user = getMessageManagement().findUserById(requestTracker,
					(String) map.get("ownerId"));

			// Only last 7 chars of Shipment/Order id
			map.put("shipId", handleObjectLastChars((String) map.get("id"), 7));
			map.put("orderIdValue",
					handleObjectLastChars((String) map.get("orderId"), 7));

			getEmailSender()
					.send(user.getEmail(),
							"contact@promelle.com",
							String.format(
									"Order # \"%s\"​; Item # \"%s\",​ has been delivered!",
									new Object[] {
											(String) map.get("orderIdValue"),
											(String) map.get("shipId") }),
							prepareMessage(readFile(SHIPMENT_DELIVERED)
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

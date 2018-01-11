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
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class ProductActivatonHandler extends CommunicationMessageHandler {

	private static final String PRODUCT_ACTIVATION = "product-activation.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("product.activated"));
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
			map.put("ownerFName", user.getName());
			System.out.println(map);
			getEmailSender()
					.send(user.getEmail(),
							"contact@promelle.com",
							"Promelle New Dress Listing Approved!",
							prepareMessage(readFile(PRODUCT_ACTIVATION)
									.toString(), map));

			// Send device notification to lender
			pushNotificationToUserDevices(
					requestTracker,
					(String) map.get("ownerId"),
					String.format(
							"Congratulations! Your listing \"%s\" on PromElle has been approved.",
							(String) map.get("title")), "");

		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

}

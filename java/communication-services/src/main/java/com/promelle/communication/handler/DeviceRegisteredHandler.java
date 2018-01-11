package com.promelle.communication.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class DeviceRegisteredHandler extends CommunicationMessageHandler {

	// private static final String PRODUCT_CREATION = "product-creation.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("device.registered"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = message.getRequestTracker();
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);

			String userId = (String) map.get("userId");
			if (StringUtils.isNotBlank(userId)) {
				String notification = String.format(
						"User # %s successfully registered.", userId);

				pushNotificationToUserDevices(requestTracker, userId,
						notification, "");
			} else {
				System.out
						.println("Requested user doesn't have any registered device.");
			}

			// getEmailSender()
			// .send("contact@promelle.com",
			// user.getEmail(),
			// "New Listing submitted by Lender! Please review and approve.",
			// prepareMessage(readFile(PRODUCT_CREATION)
			// .toString(), map));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}
}

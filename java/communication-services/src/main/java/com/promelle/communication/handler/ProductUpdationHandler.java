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
public class ProductUpdationHandler extends CommunicationMessageHandler {

	private static final String PRODUCT_UPDATION = "product-updation.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("product.updated"));
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

			getEmailSender()
					.send("contact@promelle.com",
							user.getEmail(),
							"New Listing submitted by Lender! Please review and approve.",
							prepareMessage(readFile(PRODUCT_UPDATION)
									.toString(), map));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

}

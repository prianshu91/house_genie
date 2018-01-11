package com.promelle.communication.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.dto.Product;
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
public class ProductMessageCreateHandler extends CommunicationMessageHandler {
	private static final String MESSAGE_CREATE_FILE = "product-message-create.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("product.message.created"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = message.getRequestTracker();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);
			System.out.println(map);
			Product product = getMessageManagement().findProductById(
					requestTracker, (String) map.get("productId"));
			System.out.println("product => " + product);
			map.put("productTitle", product.getTitle());
			User user = getMessageManagement().findUserById(requestTracker,
					(String) map.get("receiverId"));

			String userEmail = user.getEmail();
			if (userEmail.toLowerCase().endsWith("@mailinator.com")) {
				userEmail = userEmail + ",contact@promelle.com";
			}
			getEmailSender().send(
					userEmail,
					"contact@promelle.com",
					String.format("New message regarding the listing \"%s\"",
							product.getTitle()),
					prepareMessage(readFile(MESSAGE_CREATE_FILE).toString(),
							map));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}
}

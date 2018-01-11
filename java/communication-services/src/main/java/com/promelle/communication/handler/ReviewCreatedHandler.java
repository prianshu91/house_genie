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
 * @author Satish Sharma
 * @version 0.0.1
 */
public class ReviewCreatedHandler extends CommunicationMessageHandler {

	private static final String REVIEW_CREATED = "review-created.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("review.created"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = message.getRequestTracker();
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);

			Product product = getMessageManagement().findProductById(
					requestTracker, (String) map.get("productId"));

			User user = getMessageManagement().findUserById(requestTracker,
					product.getOwnerId());

			map.put("title", product.getTitle());

			getEmailSender().send(
					"contact@promelle.com",
					"contact@promelle.com",
					user.getEmail(),
					String.format("Review for Listing \"%s\"",
							product.getTitle()),
					prepareMessage(readFile(REVIEW_CREATED).toString(), map));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}
}

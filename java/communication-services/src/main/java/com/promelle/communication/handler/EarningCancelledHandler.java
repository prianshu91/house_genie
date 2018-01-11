package com.promelle.communication.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class EarningCancelledHandler extends CommunicationMessageHandler {
	private static final String FILE = "earning-cancelled.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("earning.cancelled"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);
			System.out.println(map);
			String userId = (String) map.get("userId");
			String ownerId = (String) map.get("ownerId");
			// getEmailSender().send(user.getEmail(), "contact@promelle.com",
			// "PromElle Account Sign Up Verification Code",
			// prepareMessage(readFile(FILE).toString(), user));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

}

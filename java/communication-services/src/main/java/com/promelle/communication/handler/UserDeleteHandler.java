package com.promelle.communication.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.dto.User;
import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class UserDeleteHandler extends CommunicationMessageHandler {
	private static final String USER_DELETE_FILE = "user-delete.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration.getProperty("user.deleted"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {

			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(message.getData(), User.class);
			getEmailSender()
					.send(user.getEmail(),
							"contact@promelle.com",
							"PromElle Account Deleted",
							prepareMessage(readFile(USER_DELETE_FILE)
									.toString(), user));

		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}
}

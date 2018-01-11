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
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class UserCreateHandler extends CommunicationMessageHandler {
	private static final String USER_CREATE_FILE = "user-create.html";
	private static final String NEW_USER_CREATE_FILE = "new-user-create.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("user.activated"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {

			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(message.getData(), User.class);
			System.out.println(user);
			getEmailSender()
					.send(user.getEmail(),
							"contact@promelle.com",
							"Welcome to PromElle!",
							prepareMessage(readFile(USER_CREATE_FILE)
									.toString(), user));

			String orgName = user.getOrganizationName();
			if (orgName.contains("#$#&#")) {
				orgName = orgName.substring(0, orgName.indexOf("#$#&#"));
			}

			user.setOrganizationName(orgName);
			// Notification email sent to 'contact@promelle.com'
			getEmailSender().send(
					"contact@promelle.com",
					"contact@promelle.com",
					"New PromElle user registration",
					prepareMessage(readFile(NEW_USER_CREATE_FILE).toString(),
							user));

			// Welcome PUSH notification to user device
			pushNotificationToUserDevices(
					message.getRequestTracker(),
					user.getId(),
					"Welcome to PromElle! Your account registration is complete.",
					"");

		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}
}

package com.promelle.communication.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.dto.BroadcastEmail;
import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class BroadcastEmailHandler extends CommunicationMessageHandler {
	private static final String BROADCAST_EMAIL = "broadcast-email.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("broadcast.email"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			BroadcastEmail broadcastEmail = new ObjectMapper().readValue(
					message.getData(), BroadcastEmail.class);
			getEmailSender().send(
					broadcastEmail.getTo(),
					broadcastEmail.getFrom(),
					broadcastEmail.getSubject(),
					prepareMessage(readFile(BROADCAST_EMAIL).toString(),
							broadcastEmail));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

}

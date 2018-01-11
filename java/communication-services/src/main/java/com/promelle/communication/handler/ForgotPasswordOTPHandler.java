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
public class ForgotPasswordOTPHandler extends CommunicationMessageHandler {
	private static final String OTP_FILE = "forgot-password-otp.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("forgot.password.otp"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			User user = new ObjectMapper().readValue(message.getData(),
					User.class);
			System.out.println(user);
			getEmailSender().send(user.getEmail(), "contact@promelle.com",
					"Reset your PromElle Password",
					prepareMessage(readFile(OTP_FILE).toString(), user));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

}

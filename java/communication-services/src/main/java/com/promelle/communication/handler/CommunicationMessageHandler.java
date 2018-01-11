package com.promelle.communication.handler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.filter.DeviceFilter;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.communication.sender.EmailSender;
import com.promelle.communication.sender.NotificationSender;
import com.promelle.communication.sender.SmsSender;
import com.promelle.constants.Punctuation;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.topic.consumer.MessageHandler;
import com.promelle.topic.producer.MessageProducer;

/**
 * This class is responsible for handling message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public abstract class CommunicationMessageHandler implements MessageHandler {

	public static final String EMAIL = "email";

	private MessageProducer messageProducer;
	private EmailSender emailSender;
	private SmsSender smsSender;
	private CommunicationManagement messageManagement;
	private NotificationSender notificationSender;

	public MessageProducer getMessageProducer() {
		return messageProducer;
	}

	public void setMessageProducer(MessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}

	public EmailSender getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public CommunicationManagement getMessageManagement() {
		return messageManagement;
	}

	public void setMessageManagement(CommunicationManagement messageManagement) {
		this.messageManagement = messageManagement;
	}

	public StringBuilder readFile(String file) throws IOException {
		StringBuilder sb = new StringBuilder();
		String activationFilePath = AbstractConfiguration.getConfigHome()
				+ Punctuation.FORWARD_SLASH.toString()
				+ AbstractConfiguration.getAppName()
				+ Punctuation.FORWARD_SLASH.toString() + EMAIL
				+ Punctuation.FORWARD_SLASH.toString() + file;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(activationFilePath)));
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		return sb;
	}

	public String prepareMessage(String message, Object context) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Writer writer = new OutputStreamWriter(os);
			MustacheFactory mf = new DefaultMustacheFactory();
			Mustache mustache = mf.compile(new StringReader(message), "sample");
			mustache.execute(writer, context);
			writer.flush();
			return new String(os.toByteArray());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return message;
		}
	}

	public NotificationSender getNotificationSender() {
		return notificationSender;
	}

	public void setNotificationSender(NotificationSender notificationSender) {
		this.notificationSender = notificationSender;
	}

	protected void pushNotificationToUserDevices(
			AbstractRequestTracker requestTracker, String id, String message,
			String environment) throws AbstractException {

		DeviceFilter filter = new DeviceFilter();
		filter.setUserId(id);
		filter.setStatus(1);

		List<String> listOfDevices = getMessageManagement().getDevices(
				requestTracker, filter);

		getNotificationSender().push(listOfDevices, message, environment);
	}
}

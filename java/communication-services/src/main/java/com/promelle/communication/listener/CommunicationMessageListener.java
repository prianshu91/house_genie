package com.promelle.communication.listener;

import com.promelle.communication.handler.CommunicationMessageHandler;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.communication.sender.EmailSender;
import com.promelle.communication.sender.NotificationSender;
import com.promelle.communication.sender.SmsSender;
import com.promelle.exception.AbstractException;
import com.promelle.topic.consumer.MessageListener;
import com.promelle.topic.producer.MessageProducer;

/**
 * This class is responsible for listening message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class CommunicationMessageListener extends
		MessageListener<CommunicationMessageHandler> {
	private MessageProducer messageProducer;
	private EmailSender emailSender;
	private SmsSender smsSender;
	private CommunicationManagement messageManagement;
	private NotificationSender notificationSender;

	public CommunicationMessageListener(MessageProducer messageProducer,
			EmailSender emailSender, SmsSender smsSender,
			CommunicationManagement messageManagement,
			NotificationSender notificationSender) {
		super();
		this.messageProducer = messageProducer;
		this.emailSender = emailSender;
		this.smsSender = smsSender;
		this.messageManagement = messageManagement;
		this.notificationSender = notificationSender;
	}

	@Override
	public void populateMessageHandler(CommunicationMessageHandler handler)
			throws AbstractException {
		handler.setMessageProducer(messageProducer);
		handler.setEmailSender(emailSender);
		handler.setSmsSender(smsSender);
		handler.setMessageManagement(messageManagement);
		handler.setNotificationSender(notificationSender);
	}

}

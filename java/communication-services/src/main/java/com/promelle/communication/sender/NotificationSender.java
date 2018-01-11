package com.promelle.communication.sender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;
import com.promelle.communication.config.APNSCredentials;
import com.promelle.exception.AbstractException;

/**
 * This class is responsible for sending PUSH notification.
 * 
 * @author Satish Sharma
 * @version 0.0.1
 */
public class NotificationSender {

	private static final Logger LOG = LogManager
			.getLogger(NotificationSender.class);

	private APNSCredentials apnsCredentials;

	public NotificationSender(APNSCredentials apnsCredentials) {
		this.apnsCredentials = apnsCredentials;
	}

	public boolean push(List<String> listOfDevices, String message, String env)
			throws AbstractException {

		if (CollectionUtils.isEmpty(listOfDevices))
			return true;

		// Code commented require for testing purpose
		// File p12File = new File(apnsCredentials.getP12FilePath());
		// if (!p12File.exists()) {
		// LOG.info(".P12 file not exist");
		// return false;
		// }

		InputStream p12FileStream = null;
		p12FileStream = getClass().getResourceAsStream(
				apnsCredentials.getP12FilePath());

		try {
			if (p12FileStream == null || p12FileStream.available() == 0) {
				LOG.info(".P12 file not exist as resources!!");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOG.info("IOS Notification => .P12 file exist.");

		// Get APNS Service Builder and Certificate
		ApnsServiceBuilder builder;
		try {
			builder = APNS.newService().withCert(p12FileStream,
					apnsCredentials.getP12AccessPass());
		} catch (Throwable t) {
			LOG.error(t.getMessage());
			return false;
		}

		// PUSH text notification on active IOS devices
		return pushIOSNotificationFromBuilder(builder, listOfDevices, message,
				env);
	}

	/**
	 * Push IOS notification message using APNS services
	 * 
	 * @param apnsServiceBuilder
	 * @param listOfDevices
	 * @param notificationMessage
	 * @param environment
	 */
	private boolean pushIOSNotificationFromBuilder(
			ApnsServiceBuilder apnsServiceBuilder, List<String> listOfDevices,
			String notificationMessage, String environment) {

		ApnsService apnsService = null;

		try {

			// APNS service instance as per environment
			if (environment != null && environment.equals("pdn")) {
				apnsServiceBuilder = apnsServiceBuilder
						.withProductionDestination();
				apnsServiceBuilder = apnsServiceBuilder
						.withFeedbackDestination("feedback.push.apple.com",
								2196);
			} else {
				apnsServiceBuilder = apnsServiceBuilder
						.withSandboxDestination();
				apnsServiceBuilder = apnsServiceBuilder
						.withFeedbackDestination(
								"feedback.sandbox.push.apple.com", 2196);
			}

			apnsService = apnsServiceBuilder.build();

			// prepare notification message body and send to devices
			apnsService.start();
			LOG.info("APNS services start!!");

			apnsService.testConnection();
			LOG.info("APNS services connection established!!");

			// collect active device ID's list and remove all inactive device
			// IDs
			listOfDevices = removeAllInactiveDevices(apnsService, listOfDevices);

			if (listOfDevices == null || listOfDevices.size() == 0) {
				LOG.info("No Active device available!!");
				return false;
			}

			if (listOfDevices.size() > 0) {

				PayloadBuilder notificationPayload = APNS.newPayload();
				notificationPayload = notificationPayload.badge(1).alertBody(
						notificationMessage);

				// Shrink notification message content if message body is too
				// long
				if (notificationPayload.isTooLong())
					notificationPayload.shrinkBody();

				String messagePayload = notificationPayload.build();

				for (String deviceToken : listOfDevices) {
					// stop is needed, else it wont go one by one
					apnsService.stop();
					ApnsNotification apnsNotification = apnsService.push(
							deviceToken, messagePayload);
					LOG.info(apnsNotification);
				}
			}

			return true;
		} catch (Exception e) {
			LOG.error("PushIOSNotification => " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (apnsService != null) {
				apnsService.stop();
				LOG.info("APNS services stop!!");
			}
		}
	}

	/**
	 * Remove all inactive devices from list of devices
	 * 
	 * @param apnsService
	 * @param listOfDevices
	 * @return a list of active devices
	 */
	private List<String> removeAllInactiveDevices(ApnsService apnsService,
			List<String> listOfDevices) {
		Map<String, Date> inactiveDevicesList = apnsService
				.getInactiveDevices();

		if (inactiveDevicesList != null && inactiveDevicesList.size() > 0) {

			if (listOfDevices != null && listOfDevices.size() > 0)
				listOfDevices.removeAll(inactiveDevicesList.keySet());
		}
		LOG.info("Active Devices => " + listOfDevices.size());
		return listOfDevices;
	}

}

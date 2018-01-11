package com.promelle.communication.handler;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
public class EarningRedeemedHandler extends CommunicationMessageHandler {
	private static final String REDEEMED_FILE = "earning-redeemed.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("earning.redeemed"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			Map<String, Object> map = new ObjectMapper().readValue(
					message.getData(), Map.class);
			System.out.println(map);

			User user = getMessageManagement().findUserById(
					message.getRequestTracker(), (String) map.get("ownerId"));

			// map.put("dispatchedOn", new SimpleDateFormat("MM/dd/yyyy")
			// .format(new Date((long) map.get("createdOn"))));
			map.put("dispatchedOn", DateTimeFormatter.ofPattern("MM/dd/yyyy")
					.format(LocalDateTime.now()));

			// Only last 7 chars of shipment id
			String orderId = handleObjectLastChars((String) map.get("orderId"),
					7);
			map.put("orderId", orderId);
			map.put("shipId",
					handleObjectLastChars((String) map.get("shipmentId"), 7));

			map.put("chequeAmount", new DecimalFormat("#.00")
					.format((Double) map.get("redeemableAmount")));

			Map<String, Object> chqAddress = (Map<String, Object>) map
					.get("chequeAddress");
			map.put("chequeAddress0", chqAddress.get("addressLine1"));

			if (chqAddress.get("addressLine2") != null
					&& ((String) chqAddress.get("addressLine2")).length() > 0) {
				map.put("chequeAddress1", chqAddress.get("addressLine2"));
				map.put("chequeAddress2",
						String.format("%s, %s", (Object[]) new String[] {
								(String) chqAddress.get("city"),
								(String) chqAddress.get("state") }));
				map.put("chequeAddress3", chqAddress.get("zipCode"));
			} else {
				map.put("chequeAddress1",
						String.format("%s, %s", (Object[]) new String[] {
								(String) chqAddress.get("city"),
								(String) chqAddress.get("state") }));
				map.put("chequeAddress2", chqAddress.get("zipCode"));
				map.put("chequeAddress3", "");
			}

			getEmailSender()
					.send(user.getEmail(),
							"contact@promelle.com",
							String.format(
									"Your earnings check for Order # %s is on its way!",
									orderId),
							prepareMessage(readFile(REDEEMED_FILE).toString(),
									map));

		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	private String handleObjectLastChars(String data, int count) {
		return data.length() > count ? data.substring(data.length() - count)
				: data;
	}
}

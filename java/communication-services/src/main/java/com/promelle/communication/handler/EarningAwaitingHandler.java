package com.promelle.communication.handler;

import java.io.IOException;
import java.text.DecimalFormat;
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
public class EarningAwaitingHandler extends CommunicationMessageHandler {
	private static final String AWAITING_FILE = "earning-awaiting.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration
				.getProperty("earning.awaiting"));
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

			// Only last 7 chars of shipment/order id
			map.put("orderId",
					handleObjectLastChars((String) map.get("orderId"), 7));
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

			getEmailSender().send("contact@promelle.com", user.getEmail(),
					"Earnings Redeemed - Check Request by Lender",
					prepareMessage(readFile(AWAITING_FILE).toString(), map));
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	private String handleObjectLastChars(String data, int count) {
		return data.length() > count ? data.substring(data.length() - count)
				: data;
	}
}

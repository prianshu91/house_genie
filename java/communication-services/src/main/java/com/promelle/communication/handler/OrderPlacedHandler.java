package com.promelle.communication.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.address.Address;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.dto.Product;
import com.promelle.communication.dto.User;
import com.promelle.exception.AbstractException;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class OrderPlacedHandler extends CommunicationMessageHandler {

	private static final String ORDER_PLACED_LENDER = "order-placed-lender.html";
	private static final String ORDER_PLACED_RENTER = "order-placed-renter.html";
	private static final String ORDER_PLACED_LENDER_DIFFERENT = "order-placed-lender-different.html";
	private static final String ORDER_PLACED_RENTER_DIFFERENT = "order-placed-renter-different.html";

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration.getProperty("order.placed"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(TopicMessage message) throws AbstractException {
		try {
			AbstractRequestTracker requestTracker = message.getRequestTracker();
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> order = mapper.readValue(message.getData(),
					Map.class);
			List<Map<String, Object>> shipments = (List<Map<String, Object>>) order
					.get("shipments");
			for (Map<String, Object> shipment : shipments) {
				List<Map<String, Object>> items = (List<Map<String, Object>>) shipment
						.get("items");
				User user = getMessageManagement().findUserById(requestTracker,
						(String) shipment.get("ownerId"));
				for (Map<String, Object> item : items) {
					String renterEmail = getMessageManagement().findUserById(
							requestTracker, (String) item.get("userId"))
							.getEmail();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ownerName", shipment.get("ownerName"));
					map.put("renterEmail", renterEmail);
					map.put("lenderEmail", user.getEmail());
					map.put("order", item);

					// Only last 7 chars of Shipment/Order id
					map.put("shipId",
							getLastChars((String) shipment.get("id"), 7));
					map.put("orderIdValue",
							getLastChars((String) item.get("orderId"), 7));

					map.put("transactionId", order.get("transactionId"));

					// Revised mail for order placed by renter
					SimpleDateFormat formatter = new SimpleDateFormat(
							"MM/dd/yyyy");
					map.put("rentalEndDate", formatter.format(new Date(
							(Long) item.get("endDate"))));

					if (((String) ((Map<String, Object>) shipment.get("user"))
							.get("schoolId"))
							.equals((String) ((Map<String, Object>) shipment
									.get("owner")).get("schoolId"))) {

						map.put("rentalStartDate", formatter.format(new Date(
								(long) item.get("startDate")
										- (long) (24 * 60 * 60 * 1000))));

						getEmailSender()
								.send("contact@promelle.com",
										"contact@promelle.com",
										user.getEmail(),
										"Congratulations! Your listing just got rented on PromElle!",
										prepareMessage(
												readFile(ORDER_PLACED_LENDER)
														.toString(), map));

						getEmailSender()
								.send("contact@promelle.com",
										"contact@promelle.com",
										renterEmail,
										"Thank you for renting on PromElle! What you need to do next..",
										prepareMessage(
												readFile(ORDER_PLACED_RENTER)
														.toString(), map));

					} else {
						map.put("rentalStartDate", formatter.format(new Date(
								(Long) item.get("startDate"))));

						StringBuilder dressLocation = new StringBuilder();
						Product product = getMessageManagement()
								.findProductById(requestTracker,
										(String) item.get("productId"));
						String dressText = "";
						if (product.getDressLocation() != null) {
							Address address = product.getDressLocation();
							dressLocation.append(StringUtils.isNotBlank(address
									.getAddressLine1()) ? address
									.getAddressLine1() : "");
							dressLocation.append(StringUtils.isNotBlank(address
									.getAddressLine2()) ? ("," + address
									.getAddressLine2()) : "");
							dressLocation.append(StringUtils.isNotBlank(address
									.getCity()) ? ("," + address.getCity())
									: "");
							dressLocation.append(StringUtils.isNotBlank(address
									.getState()) ? ("," + address.getState())
									: "");
							dressLocation.append(StringUtils.isNotBlank(address
									.getZipCode()) ? ("," + address
									.getZipCode()) : "");
							dressText = " from the following address: ";
						}

						map.put("dressLocation", dressLocation.toString());
						map.put("dressText", dressText);

						getEmailSender()
								.send("contact@promelle.com",
										"contact@promelle.com",
										user.getEmail(),
										"Congratulations! Your listing just got rented on PromElle!",
										prepareMessage(
												readFile(
														ORDER_PLACED_LENDER_DIFFERENT)
														.toString(), map));

						getEmailSender()
								.send("contact@promelle.com",
										"contact@promelle.com",
										renterEmail,
										"Thank you for renting on PromElle! What you need to do next..",
										prepareMessage(
												readFile(
														ORDER_PLACED_RENTER_DIFFERENT)
														.toString(), map));
					}

				}
			}
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	private String getLastChars(String data, int count) {
		return data.length() > count ? data.substring(data.length() - count)
				: data;
	}
}

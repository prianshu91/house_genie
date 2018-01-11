package com.promelle.payment.stripe.handler;

import java.util.HashMap;
import java.util.Map;

import com.promelle.exception.AbstractException;
import com.promelle.payment.dto.Payment;
import com.promelle.payment.dto.PaymentCustomer;
import com.promelle.payment.handler.PaymentHandler;
import com.promelle.payment.stripe.dto.StripeInfo;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;

public class StripePaymentHandler implements PaymentHandler<StripeInfo> {

	public static final String AMOUNT = "amount";
	public static final String CURRENCY = "currency";
	public static final String TOKEN_SOURCE = "source";
	public static final String DESCRIPTION = "description";
	public static final String METADATA = "metadata";
	public static final String CUSTOMER = "customer";
	public static final String SOURCE = "source";

	@Override
	public Charge handlePayment(Payment payment, StripeInfo info)
			throws AbstractException {
		Map<String, Object> chargeParams = new HashMap<>();
		chargeParams.put(AMOUNT, (int) (payment.getAmount() * 100));
		chargeParams.put(CURRENCY, "usd");
		chargeParams.put(TOKEN_SOURCE, info.getToken());
		chargeParams.put(DESCRIPTION, payment.getDescription());
		chargeParams.put(METADATA, payment.getMetaData());

		String errorMessage = "";
		try {
			RequestOptions requestOptions = RequestOptions.builder()
					.setApiKey(info.getApiKey()).build();
			return Charge.create(chargeParams, requestOptions);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		throw new AbstractException("stripe.error")
				.setCustomMessage(errorMessage);
	}

	@Override
	public Charge handlePaymentUsingCustomer(Payment payment, StripeInfo info)
			throws AbstractException {
		if (info.getCustomerId() == null)
			return null;

		Map<String, Object> chargeParams = new HashMap<>();
		chargeParams.put(AMOUNT, (int) (payment.getAmount() * 100));
		chargeParams.put(CURRENCY, "usd");
		chargeParams.put(DESCRIPTION, payment.getDescription());
		chargeParams.put(METADATA, payment.getMetaData());
		chargeParams.put(CUSTOMER, info.getCustomerId());

		String errorMessage = "";
		try {
			RequestOptions requestOptions = RequestOptions.builder()
					.setApiKey(info.getApiKey()).build();
			return Charge.create(chargeParams, requestOptions);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		throw new AbstractException("stripe.error")
				.setCustomMessage(errorMessage);
	}

	@Override
	public PaymentCustomer createStripeCustomer(Payment payment, StripeInfo info)
			throws AbstractException {

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put(DESCRIPTION, payment.getDescription());
		customerParams.put(SOURCE, info.getToken());

		String errorMessage = "";
		try {
			RequestOptions requestOptions = RequestOptions.builder()
					.setApiKey(info.getApiKey()).build();
			Customer customer = Customer.create(customerParams, requestOptions);
			Card card = (Card) customer.getSources().retrieve(
					customer.getDefaultSource(), requestOptions);

			return new PaymentCustomer(card, customer.getId());
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		throw new AbstractException("stripe.error")
				.setCustomMessage(errorMessage);
	}

	@Override
	public Card collectPaymentCard(StripeInfo info) throws AbstractException {

		String errorMessage = "";
		try {
			RequestOptions requestOptions = RequestOptions.builder()
					.setApiKey(info.getApiKey()).build();
			Customer customer = Customer.retrieve(info.getCustomerId(),
					requestOptions);

			Card card = (Card) customer.getSources().retrieve(
					customer.getDefaultSource(), requestOptions);
			return card;
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		throw new AbstractException("stripe.error")
				.setCustomMessage(errorMessage);
	}
}

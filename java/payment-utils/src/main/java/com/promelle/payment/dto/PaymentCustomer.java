package com.promelle.payment.dto;

import com.stripe.model.Card;

/**
 * This class is intended for payment customer information.
 * 
 * @author Satish Sharma
 * @version 1.0
 */

public class PaymentCustomer {

	private Card card;

	private String customerId;

	public PaymentCustomer(Card card, String customerId) {
		this.card = card;
		this.customerId = customerId;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}

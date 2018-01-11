package com.promelle.product.dto;

import com.promelle.dto.AbstractDTO;
import com.stripe.model.Card;

/**
 * This class is intended for holding payments card type information.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class PaymentCard extends AbstractDTO {

	private static final long serialVersionUID = -689340180201795582L;
	private String last4Digit;
	private int expMonth;
	private int expYear;
	private String brand;
	private String funding;

	public PaymentCard(Card card, String cardUUID) {
		setId(cardUUID);
		setLast4Digit(card.getLast4());
		setExpMonth(card.getExpMonth());
		setExpYear(card.getExpYear());
		setBrand(card.getBrand());
		setFunding(card.getFunding());
	}

	public String getLast4Digit() {
		return last4Digit;
	}

	public void setLast4Digit(String last4Digit) {
		this.last4Digit = last4Digit;
	}

	public int getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}

	public int getExpYear() {
		return expYear;
	}

	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getFunding() {
		return funding;
	}

	public void setFunding(String funding) {
		this.funding = funding;
	}

}

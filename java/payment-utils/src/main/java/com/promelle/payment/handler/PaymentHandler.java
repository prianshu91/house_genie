package com.promelle.payment.handler;

import com.promelle.exception.AbstractException;
import com.promelle.payment.dto.AppInfo;
import com.promelle.payment.dto.Payment;
import com.promelle.payment.dto.PaymentCustomer;
import com.stripe.model.Card;
import com.stripe.model.Charge;

public interface PaymentHandler<T extends AppInfo> {

	Charge handlePayment(Payment payment, T info) throws AbstractException;

	Charge handlePaymentUsingCustomer(Payment payment, T info) throws AbstractException;

	PaymentCustomer createStripeCustomer(Payment payment, T info) throws AbstractException;

	Card collectPaymentCard(T info) throws AbstractException;

}

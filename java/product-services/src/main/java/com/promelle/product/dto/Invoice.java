package com.promelle.product.dto;

import org.apache.commons.lang.StringUtils;

import com.promelle.dto.AbstractAuditDTO;
import com.promelle.product.constants.PaymentMode;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class Invoice extends AbstractAuditDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String userId;
	private String orderId;
	private String transactionId;
	private PaymentMode paymentMode;
	private Double amount;
	private Long paymentDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentMode() {
		return paymentMode != null ? paymentMode.name() : null;
	}

	public void setPaymentMode(String paymentMode) {
		if (StringUtils.isNotBlank(paymentMode)) {
			this.paymentMode = PaymentMode.valueOf(paymentMode.toUpperCase());
		}
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}

}

package com.promelle.payment.dto;

import java.text.DecimalFormat;
import java.util.Map;

public class Payment {

	private Double amount;
	private String description;
	private Map<String, Object> metaData;
	private Double deliveryCharge;
	private Double insuranceAmount;

	public Double getAmount() {
		String doubleValue = new DecimalFormat("#.00").format(amount);
		return Double.valueOf(doubleValue);
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}

	public Double getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(Double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public Double getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(Double insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

}

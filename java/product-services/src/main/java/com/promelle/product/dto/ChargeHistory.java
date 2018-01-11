package com.promelle.product.dto;

import java.util.Map;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding cart information.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ChargeHistory extends AbstractDTO {
	private static final long serialVersionUID = -6893401802017895828L;
	private String orderId;
	private Map<String, Object> charge;
	private String chargeStr;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Map<String, Object> getCharge() {
		return charge;
	}

	public void setCharge(Map<String, Object> charge) {
		this.charge = charge;
	}

	public String getChargeStr() {
		return chargeStr;
	}

	public void setChargeStr(String chargeStr) {
		this.chargeStr = chargeStr;
	}

}

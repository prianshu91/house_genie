package com.promelle.communication.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.object.AbstractObject;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class SmsCredentials extends AbstractObject {
	private static final long serialVersionUID = -8801189832247604087L;

	@JsonProperty
	private String accuntSid;

	@JsonProperty
	private String authToken;

	@JsonProperty
	private String defaultFrom;

	public String getAccuntSid() {
		return accuntSid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getDefaultFrom() {
		return defaultFrom;
	}
}

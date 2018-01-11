package com.promelle.communication.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is intended for holding all the configurations injected from the
 * external *.yml file.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class APNSCredentials {

	@JsonProperty
	private String p12FilePath;

	@JsonProperty
	private String p12AccessPass;

	public String getP12FilePath() {

		return p12FilePath;
	}

	public String getP12AccessPass() {
		return p12AccessPass;
	}

	public void setP12FilePath(String p12FilePath) {
		this.p12FilePath = p12FilePath;
	}

	public void setP12AccessPass(String p12AccessPass) {
		this.p12AccessPass = p12AccessPass;
	}

}

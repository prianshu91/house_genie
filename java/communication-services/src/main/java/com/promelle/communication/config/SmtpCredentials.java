package com.promelle.communication.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.object.AbstractObject;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class SmtpCredentials extends AbstractObject {
	private static final long serialVersionUID = -3026235945415583337L;

	@JsonProperty
    private String user;

    @JsonProperty
    private String pass;

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

}

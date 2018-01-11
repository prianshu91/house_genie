package com.promelle.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.object.AbstractObject;

/**
 * This class is intended for holding Services related info from *.yml file.
 * 
 * @author Kanak Sony
 * @version 0.0.1
 */
public class Services extends AbstractObject {
	
	private static final long serialVersionUID = -5505658434251968947L;

	@JsonProperty
    private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}

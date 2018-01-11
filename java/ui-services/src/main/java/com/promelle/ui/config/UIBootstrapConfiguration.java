package com.promelle.ui.config;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.common.config.AbstractBootstrapConfiguration;
import com.promelle.mongo.config.MongoConfiguration;

public class UIBootstrapConfiguration extends AbstractBootstrapConfiguration {

	@JsonProperty
	private MongoConfiguration mongo = new MongoConfiguration();

	@Valid
	@NotNull
	private Long sessionTimeOutMillis;

	@JsonProperty
	Map<String, String> registry = new HashMap<String, String>();

	public MongoConfiguration getMongo() {
		return mongo;
	}

	public Long getSessionTimeOutMillis() {
		return sessionTimeOutMillis;
	}

	public Map<String, String> getRegistry() {
		return registry;
	}

}

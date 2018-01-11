package com.promelle.communication.config;

import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.common.config.MessageEnabledBootstrapConfiguration;
import com.promelle.mongo.config.MongoConfiguration;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class CommunicationBootstrapConfiguration extends MessageEnabledBootstrapConfiguration {

	@Valid
	@NotNull
	@JsonProperty
	private final SmtpCredentials smtpCredentials = new SmtpCredentials();

	@Valid
	@NotNull
	@JsonProperty
	private final SmsCredentials smsCredentials = new SmsCredentials();

	@Valid
	@NotNull
	@JsonProperty
	private final MongoConfiguration mongo = new MongoConfiguration();

	@Valid
	@NotNull
	@JsonProperty
	private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

	@Valid
	@NotNull
	@JsonProperty
	private final APNSCredentials apnsCredentials = new APNSCredentials();

	public SmtpCredentials getSmtpCredentials() {
		return smtpCredentials;
	}

	public SmsCredentials getSmsCredentials() {
		return smsCredentials;
	}

	public MongoConfiguration getMongo() {
		return mongo;
	}

	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	public APNSCredentials getApnsCredentials() {
		return apnsCredentials;
	}

}

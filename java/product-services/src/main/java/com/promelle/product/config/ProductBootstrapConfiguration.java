package com.promelle.product.config;

import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.common.config.MessageEnabledBootstrapConfiguration;
import com.promelle.mongo.config.MongoConfiguration;
import com.promelle.product.dto.Services;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductBootstrapConfiguration extends MessageEnabledBootstrapConfiguration {

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
	private final Services services = new Services();


	public MongoConfiguration getMongo() {
        return mongo;
    }

	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

    public Services getServices() {
		return services;
	}
}

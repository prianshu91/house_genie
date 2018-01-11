package com.promelle.user.config;

import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.common.config.MessageEnabledBootstrapConfiguration;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserBootstrapConfiguration extends MessageEnabledBootstrapConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

}

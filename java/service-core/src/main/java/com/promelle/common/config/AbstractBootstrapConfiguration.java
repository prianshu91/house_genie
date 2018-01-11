package com.promelle.common.config;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promelle.eureka.client.ConfiguresEurekaClient;
import com.promelle.eureka.client.EurekaClientConfiguration;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AbstractBootstrapConfiguration extends Configuration implements ConfiguresEurekaClient {

    @Valid
    @NotNull
    @JsonProperty
    private EurekaClientConfiguration eureka = new EurekaClientConfiguration();

    @Override
    public EurekaClientConfiguration getEureka() {
        return eureka;
    }

}

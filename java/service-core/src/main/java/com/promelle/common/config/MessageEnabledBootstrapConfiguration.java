package com.promelle.common.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is intended for holding all the configurations injected from the external *.yml file.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class MessageEnabledBootstrapConfiguration extends AbstractBootstrapConfiguration {

    @JsonProperty
    private KafkaConfiguration kafka = new KafkaConfiguration();

    public KafkaConfiguration getKafka() {
        return kafka;
    }

}

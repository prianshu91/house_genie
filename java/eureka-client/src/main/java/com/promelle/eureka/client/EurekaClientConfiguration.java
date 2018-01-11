package com.promelle.eureka.client;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EurekaClientConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private String name;

    @Valid
    @NotNull
    @JsonProperty
    private String vipAddress;

    @Valid
    @NotNull
    @JsonProperty
    private String serverUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVipAddress() {
        return vipAddress;
    }

    public void setVipAddress(String vipAddress) {
        this.vipAddress = vipAddress;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

}

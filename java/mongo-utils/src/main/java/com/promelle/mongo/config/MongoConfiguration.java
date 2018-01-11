package com.promelle.mongo.config;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MongoConfiguration {

    @Valid
    @JsonProperty
    @NotEmpty
    private String serverList;

    @Valid
    @JsonProperty
    @NotEmpty
    private String dbName;

    public String getServerList() {
        return serverList;
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

}

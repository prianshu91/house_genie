package com.promelle.eureka.client;

import com.codahale.metrics.health.HealthCheck;
import com.netflix.appinfo.HealthCheckCallback;

public class EurekaClientHealthCheck extends HealthCheck implements HealthCheckCallback {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }

    @Override
    public boolean isHealthy() {
        try {
            return check().isHealthy();
        } catch (Exception e) {
            throw new RuntimeException("Health Check Failed", e);
        }
    }
}

package com.promelle.eureka.client;

import io.dropwizard.server.ServerFactory;

public interface ConfiguresEurekaClient {

    ServerFactory getServerFactory();

    EurekaClientConfiguration getEureka();

}

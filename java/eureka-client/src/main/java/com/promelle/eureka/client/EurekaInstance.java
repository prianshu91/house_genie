package com.promelle.eureka.client;

import io.dropwizard.lifecycle.Managed;
import io.dropwizard.server.ServerFactory;

import java.util.Collection;

import org.apache.commons.configuration.BaseConfiguration;

import com.promelle.eureka.discovery.DiscoveryMetadataProvider;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;

public class EurekaInstance implements Managed {
    private static final String EUREKA_DATACENTER_TYPE_PROP_NAME = "datacenter.type";
    private static final DynamicPropertyFactory INSTANCE = com.netflix.config.DynamicPropertyFactory.getInstance();
    private final ConfiguresEurekaClient configuration;
    private final EurekaClientHealthCheck healthCheck;
    private final Collection<DiscoveryMetadataProvider> discoveryMetadataProviders;
    private final String eurekaNamespace = "eureka.";
    private final String datacenterType;

    public EurekaInstance(ConfiguresEurekaClient configuration, EurekaClientHealthCheck healthCheck,
            Collection<DiscoveryMetadataProvider> discoveryMetadataProviders) {
        this.configuration = configuration;
        this.healthCheck = healthCheck;
        this.discoveryMetadataProviders = discoveryMetadataProviders;
        this.datacenterType = INSTANCE.getStringProperty(eurekaNamespace + EUREKA_DATACENTER_TYPE_PROP_NAME,
                "developer").get();
    }

    @Override
    public void start() throws Exception {
        EurekaClientConfiguration eurekaClientConfiguration = configuration.getEureka();
        ServerFactory serverFactory = configuration.getServerFactory();

        Integer port = DropwizardServerHelpers.getPort(serverFactory);

        BaseConfiguration baseConfiguration = new BaseConfiguration();
        baseConfiguration.setProperty(eurekaNamespace + "name", eurekaClientConfiguration.getName());
        baseConfiguration.setProperty(eurekaNamespace + "vipAddress", eurekaClientConfiguration.getVipAddress());
        baseConfiguration.setProperty(eurekaNamespace + "serviceUrl.default", eurekaClientConfiguration.getServerUrl());
        baseConfiguration.setProperty(eurekaNamespace + "port", port);

        baseConfiguration.setProperty(eurekaNamespace + "healthCheckUrl",
                String.format("http://${eureka.hostname}:%d/healthcheck", port));
        baseConfiguration.setProperty(eurekaNamespace + "secureHealthCheckUrl",
                String.format("http://${eureka.hostname}:%d/healthcheck", port));
        baseConfiguration.setProperty(eurekaNamespace + "statusPageUrl",
                String.format("http://${eureka.hostname}:%d/healthcheck", port));
        ConfigurationManager.loadPropertiesFromConfiguration(baseConfiguration);

        EurekaInstanceConfig eurekaInstanceConfig = createEurekaInstanceConfig(discoveryMetadataProviders);

        DiscoveryManager.getInstance().initComponent(eurekaInstanceConfig,
                new DefaultEurekaClientConfig(eurekaNamespace));

        DiscoveryManager.getInstance().getDiscoveryClient().registerHealthCheckCallback(healthCheck);

        markAsUp();
    }

    protected EurekaInstanceConfig createEurekaInstanceConfig(
            Collection<DiscoveryMetadataProvider> discoveryMetadataProviders) {
        if (datacenterType.toLowerCase().equals("developer")) {
            return new DeveloperMachineDataCenterInstanceConfig(eurekaNamespace, discoveryMetadataProviders);
        }
        // cloud config
        if (datacenterType.toLowerCase().equals("amazon")) {
            // return new CloudInstanceConfig(eurekaNamespace);
        }
        // return new MyDataCenterInstanceConfig(eurekaNamespace);
        throw new RuntimeException("event-mgmt: Make other InstanceConfig's work with metadata providers.");
    }

    @Override
    public void stop() throws Exception {
        markAsDown();
    }

    public void markAsUp() {
        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    public void markAsDown() {
        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
    }
}

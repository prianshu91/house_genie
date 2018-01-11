package com.promelle.eureka.discovery;

import java.util.Map;

public interface DiscoveryMetadataProvider {
    Map<String, String> getMetadata();
}

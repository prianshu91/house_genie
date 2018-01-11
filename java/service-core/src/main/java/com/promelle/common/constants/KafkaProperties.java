package com.promelle.common.constants;

public interface KafkaProperties {
    String ZK_CONNECT = "zookeeper.server:2181";
    int ZK_SESSION_TIMEOUT_MS = 10000;
    int ZK_CONNECTION_TIMEOUT_MS = 10000;
    String GROUP_ID = "group1";
    int PRODUCER_BUFFERSIZE = 64 * 1024;
    int CONNECTION_TIMEOUT = 100000;
    int RECONNECT_INTERVAL = 10000;
    int PARTITION = 0;
    String BROKER = "kafka.server:9092";
    String ENCODER = "kafka.serializer.StringEncoder";
    int NO_OF_RETRIES = 4;
    int MAKE_DEFAULT_PARTITION = 1;
    int DEFAULT_REPLICATION = 1;

}

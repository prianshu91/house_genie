package com.promelle.common.config;

import java.util.List;

import com.promelle.object.AbstractObject;

/**
 * This class is responsible for holding kafka config.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class KafkaConfiguration extends AbstractObject {
	private static final long serialVersionUID = 3176944655860527995L;
	private String zookepperUrl;
    private String brokers;
    private int sessionTimeout = 10000;
    private int connectionTimeout = 10000;
    private String groupId = "group1";
    private int producerBufferSize = 64 * 1024;
    private int reconnectInterval = 10000;
    private int partition = 0;
    private String encoder = "kafka.serializer.StringEncoder";
    private int noOfRetries = 4;
    private int makeDefaultPartition = 1;
    private List<String> handlers;

    public String getZookepperUrl() {
        return zookepperUrl;
    }

    public void setZookepperUrl(String zookepperUrl) {
        this.zookepperUrl = zookepperUrl;
    }

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getProducerBufferSize() {
        return producerBufferSize;
    }

    public void setProducerBufferSize(int producerBufferSize) {
        this.producerBufferSize = producerBufferSize;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public int getNoOfRetries() {
        return noOfRetries;
    }

    public void setNoOfRetries(int noOfRetries) {
        this.noOfRetries = noOfRetries;
    }

    public int getMakeDefaultPartition() {
        return makeDefaultPartition;
    }

    public void setMakeDefaultPartition(int makeDefaultPartition) {
        this.makeDefaultPartition = makeDefaultPartition;
    }

    public List<String> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<String> handlers) {
        this.handlers = handlers;
    }

}

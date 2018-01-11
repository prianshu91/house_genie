package com.promelle.topic.producer.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.admin.AdminUtils;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.utils.ZKStringSerializer$;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.KafkaConfiguration;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.ListUtils;

public class KafkaProducer implements MessageProducer {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(KafkaProducer.class.getName());
	private Producer<String, String> producer;
	private ZkClient zkClient;
	String topicName;
	private final Properties props = new Properties();
	private static final int MAX_REPLICATION_FACTOR = 2;

	public KafkaProducer(KafkaConfiguration configuration, String topicName) {
		String kafkaBrokers = configuration.getBrokers();
		if (StringUtils.isEmpty(kafkaBrokers)) {
			try {
				ZooKeeper zk = new ZooKeeper(configuration.getZookepperUrl(),
						10000, null);
				List<String> ids = zk.getChildren("/brokers/ids", false);
				List<String> brokers = new ArrayList<>();
				for (String id : ids) {
					JsonNode node = new ObjectMapper().readTree(new String(zk
							.getData("/brokers/ids/" + id, false, null)));
					brokers.add(JsonUtils.getStringValue(node, "host", null)
							+ ":"
							+ JsonUtils.getStringValue(node, "port", null));
				}
				kafkaBrokers = StringUtils.join(brokers, ",");
			} catch (Exception e) {
				LOGGER.error("Error in kafka producer", e);
			}
		}
		props.put("metadata.broker.list", kafkaBrokers);
		props.put("serializer.class", configuration.getEncoder());
		producer = new Producer<>(new ProducerConfig(props));
		this.topicName = topicName;
		zkClient = new ZkClient(configuration.getZookepperUrl(),
				configuration.getSessionTimeout(),
				configuration.getConnectionTimeout(),
				ZKStringSerializer$.MODULE$);
		createTopic(configuration, getDestinationName(), ListUtils
				.splitStringIntoTrimmedList(kafkaBrokers, ",").size() == 1 ? 1
				: MAX_REPLICATION_FACTOR);
	}

	@Override
	public String getDestinationName() {
		return topicName;
	}

	@Override
	public void sendMessage(TopicMessage msg) {
		producer.send(new KeyedMessage<String, String>(getDestinationName(),
				msg.toString()));
	}

	void createTopic(KafkaConfiguration configuration, String topicName,
			int replicationFactor) {
		if (!AdminUtils.topicExists(zkClient, topicName)) {
			/**
			 * def createTopic(zkClient: ZkClient, topic: String, partitions:
			 * Int, replicationFactor: Int, topicConfig: Properties = new
			 * Properties)
			 */
			AdminUtils.createTopic(zkClient, topicName,
					configuration.getMakeDefaultPartition(), replicationFactor,
					new Properties());
		}
	}

}

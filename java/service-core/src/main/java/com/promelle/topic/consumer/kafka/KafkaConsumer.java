package com.promelle.topic.consumer.kafka;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.KafkaConfiguration;
import com.promelle.topic.consumer.MessageHandler;
import com.promelle.topic.consumer.MessageListener;
import com.promelle.topic.message.TopicMessage;

public class KafkaConsumer<Z extends MessageHandler, T extends MessageListener<Z>>
		extends Thread {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(KafkaConsumer.class.getName());
	private ConsumerConnector connector;
	private T listener;
	private String destinationName;

	public KafkaConsumer(KafkaConfiguration configuration,
			String destinationName, T listener, String group) {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", configuration.getZookepperUrl());
		properties.put("group.id", group);
		connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(
				properties));
		this.destinationName = destinationName;
		this.listener = listener;
	}

	@Override
	public void run() {
		List<KafkaStream<byte[], byte[]>> streams = connector
				.createMessageStreamsByFilter(new Whitelist(destinationName), 1);
		ExecutorService executor = Executors.newFixedThreadPool(streams.size());
		ObjectMapper mapper = new ObjectMapper();
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(() -> {
				for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : stream) {
					try {
						listener.cosume(mapper.readValue(new String(
								messageAndMetadata.message()),
								TopicMessage.class));
					} catch (Exception e) {
						LOGGER.error("Error in kafka consumer", e);
					}
				}
				return "";
			});
		}
	}

}

package com.promelle.common.bootstrap;

import io.dropwizard.setup.Environment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.common.config.AbstractConfiguration;
import com.promelle.common.config.KafkaConfiguration;
import com.promelle.common.config.MessageEnabledBootstrapConfiguration;
import com.promelle.constants.Punctuation;
import com.promelle.topic.consumer.MessageHandler;
import com.promelle.topic.consumer.MessageListener;
import com.promelle.topic.consumer.kafka.KafkaConsumer;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.topic.producer.kafka.KafkaProducer;
import com.promelle.utils.ListUtils;

/**
 * This class is intended for starting the application. It registers service, ui
 * & db migration modules.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class MessageEnabledBootstrap<T extends MessageEnabledBootstrapConfiguration, E extends MessageHandler, Z extends MessageListener<E>>
		extends AbstractBootstrap<T> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MessageEnabledBootstrap.class);
	public static final String INBOUND_TOPICS = ".inbound.topics";
	private MessageProducer messageProducer;

	public abstract Z getMessageListener(KafkaConfiguration configuration)
			throws Exception;

	public MessageProducer getMessageProducer() {
		return messageProducer;
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		String topicPrefix = AbstractConfiguration.getAppName()
				+ Punctuation.UNDERSCORE.toString();
		messageProducer = new KafkaProducer(configuration.getKafka(),
				topicPrefix + getId());
		super.run(configuration, environment);
		if (getMessageListener(configuration.getKafka()) != null) {
			List<String> inbounds = ListUtils
					.splitStringIntoTrimmedList(
							AbstractConfiguration.getProperty(getId()
									+ INBOUND_TOPICS),
							Punctuation.COMMA.toString());
			for (String inbound : inbounds) {
				LOGGER.info("Registering consumer for topic: " + inbound);
				new KafkaConsumer<E, Z>(configuration.getKafka(), topicPrefix
						+ inbound,
						getMessageListener(configuration.getKafka()),
						topicPrefix + getId()).start();
			}
		}
	}

}

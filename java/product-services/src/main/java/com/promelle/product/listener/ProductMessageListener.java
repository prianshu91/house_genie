package com.promelle.product.listener;

import com.promelle.exception.AbstractException;
import com.promelle.product.handler.ProductMessageHandler;
import com.promelle.product.management.ProductManagement;
import com.promelle.topic.consumer.MessageListener;
import com.promelle.topic.producer.MessageProducer;

/**
 * This class is responsible for handling dress message.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductMessageListener extends MessageListener<ProductMessageHandler> {
	private ProductManagement dressManagement;
	private MessageProducer messageProducer;

	public ProductMessageListener(ProductManagement dressManagement,
			MessageProducer messageProducer) {
		super();
		this.dressManagement = dressManagement;
		this.messageProducer = messageProducer;
	}

	@Override
	public void populateMessageHandler(ProductMessageHandler handler)
			throws AbstractException {
		handler.setProductManagement(dressManagement);
		handler.setMessageProducer(messageProducer);
	}

}

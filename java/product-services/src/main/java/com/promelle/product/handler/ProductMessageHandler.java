package com.promelle.product.handler;

import com.promelle.product.management.ProductManagement;
import com.promelle.topic.consumer.MessageHandler;
import com.promelle.topic.producer.MessageProducer;

/**
 * This class is responsible for handling user message.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class ProductMessageHandler implements MessageHandler {

    private ProductManagement dressManagement;
    private MessageProducer messageProducer;

    public ProductManagement getProductManagement() {
        return dressManagement;
    }

    public void setProductManagement(ProductManagement dressManagement) {
        this.dressManagement = dressManagement;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public void setMessageProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

}

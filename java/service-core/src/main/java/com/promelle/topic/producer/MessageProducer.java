package com.promelle.topic.producer;

import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;

public interface MessageProducer {

    String getDestinationName();

    void sendMessage(TopicMessage msg) throws AbstractException;

}

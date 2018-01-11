package com.promelle.topic.consumer;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public interface MessageHandler {

    List<String> getTypes();

    void handle(TopicMessage message) throws AbstractException;

}

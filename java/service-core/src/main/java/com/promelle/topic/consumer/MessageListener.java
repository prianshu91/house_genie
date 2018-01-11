package com.promelle.topic.consumer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.exception.AbstractException;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for listening message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public abstract class MessageListener<T extends MessageHandler> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private Map<String, Class<T>> registry = new HashMap<>();

    public abstract void populateMessageHandler(T handler) throws Exception;

    public Map<String, Class<T>> getRegistry() {
        return registry;
    }

    public void cosume(TopicMessage message) {
        try {
            MessageHandler messageHandler = getHandler(message);
            LOGGER.debug("Message received ------- " + message + " for messageHandler ------- " + messageHandler);
            if (messageHandler == null) {
                LOGGER.error("No handler registered for message : " + message);
                return;
            }
            messageHandler.handle(message);
        } catch (Exception e) {
            LOGGER.error("Error handling message " + message, e);
        }
    }

    public void register(List<String> handlers, ClassLoader... loaders) throws AbstractException {
        List<ClassLoader> classLoaders = new LinkedList<>();
        classLoaders.add(this.getClass().getClassLoader());
        for (ClassLoader loader : loaders) {
            classLoaders.add(loader);
        }
        register(handlers, classLoaders);
    }

    public void register(List<String> handlers, List<ClassLoader> classLoaders) throws AbstractException {
        if (handlers == null || handlers.isEmpty()) {
        	LOGGER.info("No handlers registered");
            return;
        }
        for (String handler : handlers) {
            registerMessageHandler(handler, classLoaders);
        }
    }

    private void registerTypes(T messageHandler, Class<T> clazz) throws AbstractException {
        for (String type : messageHandler.getTypes()) {
            if (registry.containsKey(type)) {
                throw new AbstractException("duplicate.message.type").setCustomMessage(type + " already registered");
            }
            LOGGER.info(type + " registered");
            registry.put(type, clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerMessageHandler(String handler, List<ClassLoader> classLoaders) throws AbstractException {
    	LOGGER.info("Registering " +  handler);
        for (ClassLoader loader : classLoaders) {
            try {
                Class<T> clazz = (Class<T>) Class.forName(handler, true, loader);
                T messageHandler = (T) clazz.newInstance();
                try {
                    registerTypes(messageHandler, clazz);
                } catch (NullPointerException e) {
					throw new AbstractException("initialization.error", e)
							.setCustomMessage("Error initializing handler : "
									+ handler);
                }
                return;
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                LOGGER.trace("Error initializing handler : " + handler, e);
            }
        }
    }

    public T getHandler(TopicMessage message) throws Exception {
        if (!registry.containsKey(message.getType())) {
            return null;
        }
        T handler;
        try {
            handler = registry.get(message.getType()).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Exception("Error getting handler", e);
        }
        populateMessageHandler(handler);
        return handler;
    }

}

package com.promelle.user.listener;

import com.promelle.exception.AbstractException;
import com.promelle.topic.consumer.MessageListener;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.user.handler.UserMessageHandler;
import com.promelle.user.management.RoleManagement;
import com.promelle.user.management.UserManagement;

/**
 * This class is responsible for handling user message.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserMessageListener extends MessageListener<UserMessageHandler> {
    private UserManagement userManagement;
    private RoleManagement roleManagement;
    private MessageProducer messageProducer;

    public UserMessageListener(UserManagement userManagement, RoleManagement roleManagement,
            MessageProducer messageProducer) {
        super();
        this.userManagement = userManagement;
        this.roleManagement = roleManagement;
        this.messageProducer = messageProducer;
    }

    @Override
    public void populateMessageHandler(UserMessageHandler handler) throws AbstractException {
        handler.setUserManagement(userManagement);
        handler.setRoleManagement(roleManagement);
        handler.setMessageProducer(messageProducer);
    }

}

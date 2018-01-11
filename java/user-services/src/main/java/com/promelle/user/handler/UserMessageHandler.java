package com.promelle.user.handler;

import com.promelle.topic.consumer.MessageHandler;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.user.management.RoleManagement;
import com.promelle.user.management.UserManagement;

/**
 * This class is responsible for handling user message.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class UserMessageHandler implements MessageHandler {

    private UserManagement userManagement;
    private RoleManagement roleManagement;
    private MessageProducer messageProducer;

    public UserManagement getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    public RoleManagement getRoleManagement() {
        return roleManagement;
    }

    public void setRoleManagement(RoleManagement roleManagement) {
        this.roleManagement = roleManagement;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public void setMessageProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

}

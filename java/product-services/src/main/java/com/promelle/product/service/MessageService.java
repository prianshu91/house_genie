package com.promelle.product.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface MessageService {

	Response readConversation(String conversationId, String userId,
			HttpServletRequest request) throws AbstractException;

	Response deleteConversation(String conversationId, String userId,
			HttpServletRequest request) throws AbstractException;

	Response deleteMessage(String conversationId, String userId,
			HttpServletRequest request) throws AbstractException;

}

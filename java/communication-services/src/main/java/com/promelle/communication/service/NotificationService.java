package com.promelle.communication.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public interface NotificationService {

	Response pushNotification(String data, HttpServletRequest request) throws AbstractException;

}

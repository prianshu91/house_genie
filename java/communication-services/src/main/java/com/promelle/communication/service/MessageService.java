package com.promelle.communication.service;

import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface MessageService {

	Response getUsers() throws AbstractException;

}

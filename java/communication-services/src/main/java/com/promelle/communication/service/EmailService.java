package com.promelle.communication.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface EmailService {

	Response sendEmail(String data, HttpServletRequest request)
			throws AbstractException;

	Response sendEmailToAdmin(String data, HttpServletRequest request)
			throws AbstractException;

	Response broadcastEmail(String data, HttpServletRequest request)
			throws AbstractException;

}

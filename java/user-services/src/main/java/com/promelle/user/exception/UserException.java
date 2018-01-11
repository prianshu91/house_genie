package com.promelle.user.exception;

import com.promelle.exception.AbstractException;

/**
 * This class is responsible for handling user exception.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserException extends AbstractException {
	private static final long serialVersionUID = 314669672628576619L;

	public UserException() {
		super();
	}

	public UserException(String code) {
		super(code);
	}

	public UserException(Throwable cause) {
		super(cause);
	}

	public UserException(String code, Throwable cause) {
		super(code, cause);
	}


}

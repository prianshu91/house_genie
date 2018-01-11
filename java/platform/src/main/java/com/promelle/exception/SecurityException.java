package com.promelle.exception;

/**
 * This class is responsible for handling security exceptions.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class SecurityException extends AbstractException {
	private static final long serialVersionUID = 1969869L;

	public SecurityException() {
	}

	public SecurityException(String code) {
		super(code);
	}

	public SecurityException(Throwable cause) {
		super("security.exception", cause);
	}

	public SecurityException(String code, Throwable cause) {
		super(code, cause);
	}

}

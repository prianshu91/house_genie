package com.promelle.exception;

/**
 * This class is responsible for marking issues during initialization.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class InitializationException extends RuntimeException {
	private static final long serialVersionUID = 123489239L;

	public InitializationException() {
		super();
	}

	public InitializationException(String code) {
		super(code);
	}

	public InitializationException(Throwable cause) {
		super("initialization.error", cause);
	}

	public InitializationException(String code, Throwable cause) {
		super(code, cause);
	}

}

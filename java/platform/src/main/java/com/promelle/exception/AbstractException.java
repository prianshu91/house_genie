package com.promelle.exception;

/**
 * This class is base class for all exceptions.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AbstractException extends Exception {
	private static final long serialVersionUID = 123489239L;

	private final String code;
	private String customMessage;

	public AbstractException() {
		super();
		code = null;
	}

	public AbstractException(String code) {
		super();
		this.code = code;
	}

	public AbstractException(Throwable cause) {
		super(cause);
		this.code = null;
	}

	public AbstractException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public AbstractException setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
		return this;
	}

}

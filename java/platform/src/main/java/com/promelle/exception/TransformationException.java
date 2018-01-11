package com.promelle.exception;

/**
 * This class is responsible for handling transformation exceptions.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class TransformationException extends AbstractException {
	private static final long serialVersionUID = 1969869L;

	public TransformationException() {
	}

	public TransformationException(String code) {
		super(code);
	}

	public TransformationException(Throwable cause) {
		super("transformation.exception", cause);
	}

	public TransformationException(String code, Throwable cause) {
		super(code, cause);
	}

}

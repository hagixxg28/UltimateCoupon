package com.hagi.couponsystem.exceptions;

import com.hagi.couponsystem.Enums.ErrorTypes;

public class ApplicationException extends Exception {
	private ErrorTypes errorType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorTypes getErrorType() {
		return errorType;
	}

	public ApplicationException(ErrorTypes type) {
		this.errorType = type;
	}

}

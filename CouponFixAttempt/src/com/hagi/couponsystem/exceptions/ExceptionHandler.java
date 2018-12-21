package com.hagi.couponsystem.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.hagi.couponsystem.beans.ErrorBean;

public class ExceptionHandler implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		if (exception instanceof ApplicationException) {
			ApplicationException exep = (ApplicationException) exception;
			int internalErrorCode = exep.getErrorType().getErrorCode();
			String internalMessage = exep.getMessage();
			String externalMessage = exep.getErrorType().GetErrorMessage();
			ErrorBean Bean = new ErrorBean(internalErrorCode, internalMessage, externalMessage);
			return Response.status(internalErrorCode).entity(Bean).build();

		}
		String internalMessage = exception.getMessage();
		ErrorBean Bean = new ErrorBean(649, internalMessage, "General Error");
		return Response.status(649).entity(Bean).build();

	}

}

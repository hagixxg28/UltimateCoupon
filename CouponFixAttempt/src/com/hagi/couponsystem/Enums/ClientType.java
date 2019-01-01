package com.hagi.couponsystem.Enums;

import com.hagi.couponsystem.exceptions.ApplicationException;

public enum ClientType {
	CUSTOMER, COMPANY, ADMIN;

	public static ClientType parseType(String type) throws ApplicationException {
		ClientType returnType = null;
		switch (type) {
		case "customer":
			returnType = ClientType.CUSTOMER;
			return returnType;
		case "company":
			returnType = ClientType.COMPANY;
			return returnType;
		case "admin":
			returnType = ClientType.ADMIN;
			return returnType;

		default:
			throw new ApplicationException(ErrorTypes.INVALID_CLIENT_TYPE);
		}
	}
	
	 ClientType() {
		
	}
}

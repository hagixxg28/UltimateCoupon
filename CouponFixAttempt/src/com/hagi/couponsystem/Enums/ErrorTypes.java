package com.hagi.couponsystem.Enums;

public enum ErrorTypes {

	COMPANY_ALREADY_EXISTS(650, "This company already exists"),
	COMPANY_DOSENT_EXIST(651, "This company does not exist"), COUPON_ALREADY_EXISTS(652, "This coupon already exists"),
	COUPON_DOSENT_EXIST(653, "This coupon does not exist"),
	CUSTOMER_ALREADY_EXISTS(654, "This customer already exists"),
	CUSTOMER_DOSENT_EXIST(655, "This customer already exists"),
	NO_COMPANIES(656, "There are no companies in the database"),
	NO_CUSTOMERS(657, "There are no customers in the database"),
	NO_COUPONS(658, "There are no coupons in the database"), COUPON_EXPIRED(659, "This coupon has expired"),
	OUT_OF_COUPONS(660, "This coupon is out of stock"),
	FAILED_TO_CLOSE(661, "Failed to close the ResultSet or PreparedStatement"), FAILED_TO_LOGIN(662, "Failed to login"),
	CUSTOMER_OWNS_COUPON(663, "This customer already owns this coupon"),
	SAME_TITLE(664, "A coupon with this title already exists"), INVALID_TYPE(665, "Invalid coupon type"),
	SYSTEM_FAILFURE(666, "SYSTEM FAILURE"), INVALID_CLIENT_TYPE(667, "Invalid clinet type");

	private int errorCode;
	private String errorMessage;

	ErrorTypes(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

	}

	public int getErrorCode() {
		return errorCode;
	}

	public String GetErrorMessage() {
		return errorMessage;
	}
}

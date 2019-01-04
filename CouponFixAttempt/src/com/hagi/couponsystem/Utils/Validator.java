package com.hagi.couponsystem.Utils;

import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Customer;

public class Validator {

	private static final int MIN_NAME_LENGTH = 3;
	private static final int MIN_PASSWORD_LENGTH = 3;

	public static void validateAndSetCompany(Company newCompany, Company oldCompany) {
		if (validatePassword(newCompany.getPassword())) {
			oldCompany.setPassword(newCompany.getPassword());
		}
		if (validateEmail(newCompany.getEmail())) {
			oldCompany.setEmail(newCompany.getEmail());
		}
		if (validateEmail(newCompany.getCompName())) {
			oldCompany.setCompName(newCompany.getCompName());
		}
	}

	public static void validateAndSetCustomer(Customer newCustomer, Customer oldCustomer) {
		if (validatePassword(newCustomer.getPassword())) {
			oldCustomer.setPassword(newCustomer.getPassword());
		}
		if (validateName(newCustomer.getCustName())) {
			oldCustomer.setCustName(newCustomer.getCustName());
		}
	}

	public static boolean validatePassword(String password) {
		if (password != null && password.length() >= MIN_PASSWORD_LENGTH) {
			return true;
		}
		return false;
	}

	public static boolean validateEmail(String email) {
		if (email != null && email.contains("@")) {
			return true;
		}
		return false;
	}

	public static boolean validateName(String name) {
		if (name != null && name.length() >= MIN_NAME_LENGTH) {
			return true;
		}
		return false;
	}
}

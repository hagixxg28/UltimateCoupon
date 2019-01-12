package com.hagi.couponsystem.Utils;

import java.sql.Date;
import java.util.Collection;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class Validator {

	private static final int MIN_NAME_LENGTH = 3;
	private static final int MIN_TEXT_LENGTH = 3;
	private static final int MIN_PRICE_VALUE = 1;
	private static final int MIN_AMOUNT_VALUE = 1;
	private static final int MIN_PASSWORD_LENGTH = 3;

	public static void validateAndSetCoupon(Coupon newCoupon, Coupon oldCoupon) {
		if (newCoupon.getAmount() > 0) {
			oldCoupon.setAmount(newCoupon.getAmount());
		}
		Date now = new Date(System.currentTimeMillis());
		if (newCoupon.getEndDate() != null && newCoupon.getEndDate().after(now)) {
			oldCoupon.setEndDate(newCoupon.getEndDate());
		}

		if (newCoupon.getMessage() != null && newCoupon.getMessage().length() >= MIN_TEXT_LENGTH) {
			oldCoupon.setMessage(newCoupon.getMessage());
		}

		if (newCoupon.getImage() != null && newCoupon.getImage().length() > 0) {
			oldCoupon.setImage(newCoupon.getImage());
		}

		if (newCoupon.getMessage() != null && newCoupon.getMessage().length() >= MIN_TEXT_LENGTH) {
			oldCoupon.setMessage(newCoupon.getMessage());
		}

		if (newCoupon.getPrice() > 0) {
			oldCoupon.setPrice(newCoupon.getPrice());
		}

		if (newCoupon.getTitle() != null && newCoupon.getTitle().length() >= MIN_TEXT_LENGTH) {
			oldCoupon.setTitle(newCoupon.getTitle());
		}

		if (newCoupon.getType() != null && newCoupon.getType() instanceof CouponType) {
			oldCoupon.setType(newCoupon.getType());
		}

	}

	public static void validateAndSetCompany(Company newCompany, Company oldCompany) {
		if (validatePassword(newCompany.getPassword())) {
			oldCompany.setPassword(newCompany.getPassword());
		}
		if (validateEmail(newCompany.getEmail())) {
			oldCompany.setEmail(newCompany.getEmail());
		}
		if (validateName(newCompany.getcompName())) {
			oldCompany.setcompName(newCompany.getcompName());
		}
	}

	public static void validateCompany(Company Company) throws ApplicationException {
		if (validatePassword(Company.getPassword())) {
			if (validateEmail(Company.getEmail())) {
				if (validateName(Company.getcompName())) {
					return;
				}
			}
		}
		throw new ApplicationException(ErrorTypes.INVALID_COMPANY_CREATION);
	}

	public static void validateCoupon(Coupon coupon) throws ApplicationException {
		endDateValidaotr(coupon.getEndDate());
		priceValidator(coupon.getPrice());
		amountValidator(coupon.getAmount());
		textValidator(coupon.getTitle());
		textValidator(coupon.getMessage());
	}

	public static void validateCustomer(Customer customer) throws ApplicationException {
		if (validatePassword(customer.getPassword())) {
			if (validateName(customer.getcustName())) {
				return;
			}
		}
		throw new ApplicationException(ErrorTypes.INVALID_CUSTOMER_CREATION);
	}

	public static void validateAndSetCustomer(Customer newCustomer, Customer oldCustomer) {
		if (validatePassword(newCustomer.getPassword())) {
			oldCustomer.setPassword(newCustomer.getPassword());
		}
		if (validateName(newCustomer.getcustName())) {
			oldCustomer.setcustName(newCustomer.getcustName());
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

	public static void amountValidatorAndCollectionCleaner(Collection<Coupon> collection) {
		for (Coupon coupon : collection) {
			if (coupon.getAmount() <= 0) {
				collection.remove(coupon);
			}
		}
	}

	public static void priceValidator(double price) throws ApplicationException {
		if (price <= MIN_PRICE_VALUE) {
			throw new ApplicationException(ErrorTypes.INVALID_PRICE);
		}
	}

	public static void validateCollectionCoupon(Collection<Coupon> collection) throws ApplicationException {
		if (collection.isEmpty()) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		}
	}

	public static void validateCollectionCompany(Collection<Company> collection) throws ApplicationException {
		if (collection.isEmpty()) {
			throw new ApplicationException(ErrorTypes.NO_COMPANIES);
		}
	}

	public static void validateCollectionCustomer(Collection<Customer> collection) throws ApplicationException {
		if (collection.isEmpty()) {
			throw new ApplicationException(ErrorTypes.NO_CUSTOMERS);
		}
	}

	public static void endDateValidaotr(Date date) throws ApplicationException {
		Date now = new Date(System.currentTimeMillis());
		if (date.before(now)) {
			throw new ApplicationException(ErrorTypes.INVALD_END_DATE);
		}
	}

	public static void amountValidator(int amount) throws ApplicationException {
		if (amount <= MIN_AMOUNT_VALUE) {
			throw new ApplicationException(ErrorTypes.INVALD_AMOUNT);
		}
	}

	public static void textValidator(String text) throws ApplicationException {
		if (text.length() <= MIN_TEXT_LENGTH) {
			throw new ApplicationException(ErrorTypes.INVALD_TEXT_LENGTH);
		}
	}

}

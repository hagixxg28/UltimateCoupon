package com.hagi.couponsystem.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class Extractor {

	public static Company extractCompanyFromResultSet(ResultSet resultSet) throws ApplicationException, SQLException {
		Company company = new Company();
		company.setId(resultSet.getLong("comp_id"));
		company.setCompName(resultSet.getString("name"));
		company.setPassword(resultSet.getString("password"));
		company.setEmail(resultSet.getString("email"));
		return company;
	}

	public static Customer extractCustomerFromResultSet(ResultSet resultSet) throws ApplicationException, SQLException {
		Customer customer = new Customer();
		customer.setId(resultSet.getLong("cust_id"));
		customer.setCustName(resultSet.getString("name"));
		customer.setPassword(resultSet.getString("password"));
		return customer;
	}

	public static Coupon extractCouponFromResultSet(ResultSet resultSet) throws ApplicationException, SQLException {
		Coupon coupon = new Coupon();
		coupon.setId(resultSet.getLong("coup_id"));
		coupon.setTitle(resultSet.getString("title"));
		coupon.setStartDate(resultSet.getDate("start_date"));
		coupon.setEndDate(resultSet.getDate("end_date"));
		coupon.setAmount(resultSet.getInt("amount"));
		coupon.setType(CouponType.typeSort(resultSet.getString("type")));
		coupon.setMessage(resultSet.getString("message"));
		coupon.setPrice(resultSet.getDouble("price"));
		coupon.setImage(resultSet.getString("image"));
		coupon.setCompId(resultSet.getLong("comp_id"));
		return coupon;
	}

}

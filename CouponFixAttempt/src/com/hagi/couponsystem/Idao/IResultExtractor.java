package com.hagi.couponsystem.Idao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exceptions.ApplicationException;

public interface IResultExtractor {

	Company extractCompanyFromResultSet(ResultSet resultSet) throws ApplicationException, SQLException;

	Customer extractCustomerFromResultSet(ResultSet resultSet) throws ApplicationException, SQLException;

	Coupon extractCouponFromResultSet(ResultSet resultSet) throws ApplicationException, SQLException;
}

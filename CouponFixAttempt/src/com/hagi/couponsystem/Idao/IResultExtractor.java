package com.hagi.couponsystem.Idao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exception.dao.DaoException;

public interface IResultExtractor {

	Company extractCompanyFromResultSet(ResultSet resultSet) throws DaoException, SQLException;

	Customer extractCustomerFromResultSet(ResultSet resultSet) throws DaoException, SQLException;

	Coupon extractCouponFromResultSet(ResultSet resultSet) throws DaoException, SQLException;
}

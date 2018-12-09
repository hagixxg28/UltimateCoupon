package com.hagi.couponsystem.Idao;

import java.util.Collection;

import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exception.dao.DaoException;

public interface ICustomerDao {
	void createCustomer(Customer cust) throws DaoException;

	void removeCustomer(Long id) throws DaoException;

	void updateCustomer(Customer cust) throws DaoException;

	Customer getCustomer(Long id) throws DaoException;

	Collection<Customer> getAllCustomer() throws DaoException;

//	Collection<Customer> getAllCustomerWithCoupons() throws DaoException;

	Boolean login(Long id, String password) throws DaoException;

	Boolean customerExists(Long id) throws DaoException;

}

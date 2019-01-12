package com.hagi.couponsystem.oldIDao;

import java.util.Collection;

import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exceptions.ApplicationException;

public interface ICustomerDao {
	void createCustomer(Customer cust) throws ApplicationException;

	void removeCustomer(Long id) throws ApplicationException;

	void updateCustomer(Customer cust) throws ApplicationException;

	Customer getCustomer(Long id) throws ApplicationException;

	Collection<Customer> getAllCustomer() throws ApplicationException;

//	Collection<Customer> getAllCustomerWithCoupons() throws ApplicationException;

	Boolean login(Long id, String password) throws ApplicationException;

	Boolean customerExists(Long id) throws ApplicationException;

}

package com.hagi.couponsystem.logic;

import java.util.Collection;

import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.exception.dao.DaoException;
import com.hagi.couponsystem.exception.facade.FacadeException;

public class CustomerLogic {
	private ICustomerDao custDb = new CustomerDao();

	public CustomerLogic() {

	}

	public void createCustomer(Customer customer) throws FacadeException {
		try {
			if (custDb.customerExists(customer.getId())) {
				throw new FacadeException("Customer already exists");
			}
			custDb.createCustomer(customer);
		} catch (DaoException e) {
			throw new FacadeException("Failed to create");
		}
	}

	public Customer getCustomer(long customerId) throws FacadeException {
		try {
			if (custDb.customerExists(customerId)) {
				return custDb.getCustomer(customerId);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new FacadeException("No customer with this id was found");
		}
		return null;
	}

	public void updateCustomer(Customer customer) throws FacadeException {
		try {
			if (custDb.customerExists(customer.getId())) {
				custDb.updateCustomer(customer);
				return;
			}
		} catch (DaoException e) {
			throw new FacadeException("No customer with this id exists");
		}
	}

	public void removeCustomer(long customerId) throws FacadeException {
		try {
			if (custDb.customerExists(customerId)) {
				custDb.removeCustomer(customerId);
			}
		} catch (DaoException e) {
			throw new FacadeException("No customer with this id was found");
		}
	}

	public Collection<Customer> getAllCustomers() throws FacadeException {
		try {
			return custDb.getAllCustomer();
		} catch (DaoException e) {
			throw new FacadeException("No customers on database");
		}
	}
}

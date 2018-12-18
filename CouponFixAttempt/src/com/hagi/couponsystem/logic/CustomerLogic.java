package com.hagi.couponsystem.logic;

import java.util.Collection;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CustomerLogic {
	private ICustomerDao custDb = CustomerDao.getInstance();
	private static CustomerLogic instance;

	private CustomerLogic() {
		super();
	}

	public static CustomerLogic getInstance() {
		if (instance == null) {
			instance = new CustomerLogic();
		}
		return instance;
	}

	public void createCustomer(Customer customer) throws ApplicationException {
		if (!custDb.customerExists(customer.getId())) {
			custDb.createCustomer(customer);
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_ALREADY_EXISTS);
	}

	public Customer getCustomer(long customerId) throws ApplicationException {
		return custDb.getCustomer(customerId);
	}

	public void updateCustomer(Customer customer) throws ApplicationException {
		if (custDb.customerExists(customer.getId())) {
			custDb.updateCustomer(customer);
			return;
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}

	public void removeCustomer(long customerId) throws ApplicationException {
		if (custDb.customerExists(customerId)) {
			custDb.removeCustomer(customerId);
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}

	public Collection<Customer> getAllCustomers() throws ApplicationException {
		return custDb.getAllCustomer();
	}
}

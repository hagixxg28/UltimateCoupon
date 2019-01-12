package com.hagi.couponsystem.logic;

import java.util.Collection;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.Utils.Validator;
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
			Validator.validateCustomer(customer);
			custDb.createCustomer(customer);
			return;
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_ALREADY_EXISTS);
	}

	public Customer getCustomer(long customerId) throws ApplicationException {
		if (custDb.customerExists(customerId)) {
			return custDb.getCustomer(customerId);
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}

	public void updateCustomer(Customer customer) throws ApplicationException {
		Customer updateCustomer = custDb.getCustomer(customer.getId());
		Validator.validateAndSetCustomer(customer, updateCustomer);
		custDb.updateCustomer(updateCustomer);
	}

	public void removeCustomer(long customerId) throws ApplicationException {
		if (custDb.customerExists(customerId)) {
			custDb.removeCustomer(customerId);
			return;
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}

	public Collection<Customer> getAllCustomers() throws ApplicationException {
		Collection<Customer> list = custDb.getAllCustomer();
		Validator.validateCollectionCustomer(list);
		return list;
	}
}

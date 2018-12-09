package come.hagi.couponsystem.oldfacade;

import java.util.Collection;

import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.exception.dao.CompanyAlreadyExistsException;
import com.hagi.couponsystem.exception.dao.DaoException;
import com.hagi.couponsystem.exception.dao.NoCompaniesException;
import com.hagi.couponsystem.exception.dao.NoCustomersException;
import com.hagi.couponsystem.exception.facade.FacadeException;

public class AdminFacade implements CouponClientFacade {

	private ICustomerDao custDb = new CustomerDao();
	private ICompanyDao compDb = new CompanyDao();
//	private ICouponDao coupDb = new CouponDao();

	public AdminFacade() {

	}

	public void createCompany(Company comp) throws FacadeException {
		try {
			if (!compDb.companyExists(comp.getId())) {
				compDb.createCompany(comp);
			} else {
				throw new CompanyAlreadyExistsException("A company with this name already exists");
			}
		} catch (DaoException e) {
			throw new FacadeException("Something went wrong");
		}
	}

//	public void removeCompany(Long id) throws FacadeException {
//		try {
//			if (compDb.companyExists(comp)) {
//				compDb.removeCompany(comp);
//				for (Coupon coup : compDb.getAllCoupons(comp)) {
//					coupDb.fullyRemoveCoupon(coup);
//				}
//			} else {
//				throw new CompanyDoesNotExistException("This company dosen't exist");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("Company was not found");
//		}
//	}

//	public void updateCompany(Company comp) throws FacadeException {
//		try {
//			if (compDb.companyExists(comp)) {
//				compDb.updateCompany(comp);
//			} else {
//				throw new CompanyDoesNotExistException("This company dosen't exist");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("Company was not found");
//		}
//	}

//	public Company getCompany(Company comp) throws FacadeException {
//		try {
//			if (compDb.companyExists(comp)) {
//				return compDb.readCompany(comp);
//			} else {
//				throw new CompanyDoesNotExistException("This company dosen't exist");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("Company was not found");
//		}
//	}

	public Collection<Company> getAllCompanies() throws FacadeException {
		try {
			if (!compDb.getAllCompanies().isEmpty()) {
				return compDb.getAllCompanies();
			} else {
				throw new NoCompaniesException("There are no companies on the database");
			}
		} catch (DaoException e) {
			throw new FacadeException("No companies were found");
		}
	}

//	public void createCustomer(Customer cust) throws FacadeException {
//		try {
//			if (!custDb.customerExists(cust)) {
//				custDb.createCustomer(cust);
//			} else {
//				throw new CustomerAlreadyExistsException("A customer with this id already exists");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("A customer with this id already exists");
//		}
//	}

//	public void removeCustomer(Customer cust) throws FacadeException {
//		try {
//			if (custDb.customerExists(cust)) {
//				custDb.removeCustomer(cust);
//				if (!cust.getCouponHistory().isEmpty()) {
//					cust.getCouponHistory().clear();
//				}
//			} else {
//				throw new CustomerDoesNotExistException("This customer does not exist");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("This customer does not exist");
//		}
//
//	}

//	public void updateCustomer(Customer cust) throws FacadeException {
//		try {
//			if (custDb.customerExists(cust)) {
//				custDb.updateCustomer(cust);
//			} else {
//				throw new CustomerDoesNotExistException("This customer does not exist");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("This customer does not exist");
//		}
//	}

//	public Customer getCustomer(Customer cust) throws FacadeException {
//		try {
//			if (custDb.customerExists(cust)) {
//				return custDb.getCustomer(cust);
//			} else {
//				throw new CustomerDoesNotExistException("This customer does not exist");
//			}
//		} catch (DaoException e) {
//			throw new FacadeException("Something went wrong");
//		}
//	}

	public Collection<Customer> getAllCustomer() throws FacadeException {
		try {
			if (!custDb.getAllCustomer().isEmpty()) {
				return custDb.getAllCustomer();
			} else {
				throw new NoCustomersException("there are no customers on the database");
			}
		} catch (DaoException e) {
			throw new FacadeException("There are no customers on the Database");
		}
	}
}

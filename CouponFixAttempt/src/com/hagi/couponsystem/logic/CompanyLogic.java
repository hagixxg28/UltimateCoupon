package com.hagi.couponsystem.logic;

import java.util.Collection;

import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.exception.dao.DaoException;
import com.hagi.couponsystem.exception.facade.FacadeException;

public class CompanyLogic {
	private ICompanyDao compDb = new CompanyDao();

	public CompanyLogic() {

	}

	public Company getcompany(long companyId) throws FacadeException {
		try {
			if (compDb.companyExists(companyId)) {
				return compDb.readCompany(companyId);
			}
			throw new FacadeException("Company not found");
		} catch (DaoException e) {
			throw new FacadeException("Company not found");
		}
	}

	public void createCompany(Company company) throws FacadeException {
		try {
			if (!compDb.companyExists(company.getId())) {
				compDb.createCompany(company);
			} else {
				throw new FacadeException("Company exists");
			}
		} catch (DaoException e) {
			throw new FacadeException("something");
		}
	}

	public void updateCompany(Company company) throws FacadeException {
		try {
			if (compDb.companyExists(company.getId())) {
				compDb.updateCompany(company);
			}
		} catch (DaoException e) {
			throw new FacadeException("Company not found");
		}
	}

	public void removeCompany(long companyId) throws FacadeException {
		try {
			if (compDb.companyExists(companyId)) {
				compDb.removeCompany(companyId);
			}
		} catch (DaoException e) {
			throw new FacadeException("Company not found");
		}
	}

	public Collection<Company> getAllCompanies() throws FacadeException {
		try {
			return compDb.getAllCompanies();
		} catch (DaoException e) {
			throw new FacadeException("No cmpanies were found");
		}
	}
}

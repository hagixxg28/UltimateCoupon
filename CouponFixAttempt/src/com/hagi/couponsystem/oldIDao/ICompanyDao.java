package com.hagi.couponsystem.oldIDao;

import java.util.Collection;

import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.exceptions.ApplicationException;

public interface ICompanyDao {

	void createCompany(Company comp) throws ApplicationException;

	void removeCompany(Long id) throws ApplicationException;

	void updateCompany(Company comp) throws ApplicationException;

	Company readCompany(Long id) throws ApplicationException;

	Collection<Company> getAllCompanies() throws ApplicationException;

	Boolean login(Long id, String password) throws ApplicationException;

	Boolean companyExists(Long id) throws ApplicationException;

}

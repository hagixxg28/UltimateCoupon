package com.hagi.couponsystem.Idao;

import java.util.Collection;

import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.exception.dao.DaoException;

public interface ICompanyDao {

	void createCompany(Company comp) throws DaoException;

	void removeCompany(Long id) throws DaoException;

	void updateCompany(Company comp) throws DaoException;

	Company readCompany(Long id) throws DaoException;

	Collection<Company> getAllCompanies() throws DaoException;

	Boolean login(Long id, String password) throws DaoException;

	Boolean companyExists(Long id) throws DaoException;

}

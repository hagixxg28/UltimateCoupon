package com.hagi.couponsystem.logic;

import java.util.Collection;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Utils.Validator;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CompanyLogic {
	private ICompanyDao compDb = CompanyDao.getInstance();
	private static CompanyLogic instance;

	private CompanyLogic() {
		super();
	}

	public static CompanyLogic getInstance() {
		if (instance == null) {
			instance = new CompanyLogic();
		}
		return instance;
	}

	public Company getcompany(long companyId) throws ApplicationException {
		if (compDb.companyExists(companyId)) {
			return compDb.readCompany(companyId);
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public void createCompany(Company company) throws ApplicationException {
		if (!compDb.companyExists(company.getId())) {
			compDb.createCompany(company);
			return;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_ALREADY_EXISTS);
	}

	public void updateCompany(Company company) throws ApplicationException {
		// This checks if the company exists and gets it
		Company updateCompany = compDb.readCompany(company.getId());
		//The Validator makes sure the values we got are not null and are up to our standart, if they are he will set it into the update object that we send to the SQL
		Validator.validateAndSetCompany(company, updateCompany);
		compDb.updateCompany(updateCompany);

	}

	public void removeCompany(long companyId) throws ApplicationException {
		if (compDb.companyExists(companyId)) {
			compDb.removeCompany(companyId);
			return;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public Collection<Company> getAllCompanies() throws ApplicationException {
		return compDb.getAllCompanies();
	}
}

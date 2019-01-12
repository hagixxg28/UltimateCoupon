package com.hagi.couponsystem.logic;

import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Idao.ICouponDao;
import com.hagi.couponsystem.Utils.Validator;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CouponDao;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CompanyLogic {
	private ICompanyDao compDb = CompanyDao.getInstance();
	private ICouponDao coupDb = CouponDao.getInstance();
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
			Validator.validateCompany(company);
			compDb.createCompany(company);
			return;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_ALREADY_EXISTS);
	}

	public void updateCompany(Company company) throws ApplicationException {
		// This checks if the company exists and gets it
		if (compDb.companyExists(company.getId())) {
			Company updateCompany = compDb.readCompany(company.getId());
			// The Validator makes sure the values we got are not null and are up to our
			// standard, if they are he will set it into the update object that we send to
			// the SQL
			Validator.validateAndSetCompany(company, updateCompany);
			compDb.updateCompany(updateCompany);
			return;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);

	}

	public void removeCompany(long companyId) throws ApplicationException {
		if (compDb.companyExists(companyId)) {
			Collection<Coupon> list = new ArrayList<>();
			list = coupDb.getAllCouponsForCompany(companyId);

			for (Coupon coupon : list) {
				coupDb.fullyRemoveCoupon(coupon.getId());
			}

			compDb.removeCompany(companyId);
			return;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public Collection<Company> getAllCompanies() throws ApplicationException {
		Collection<Company> list = new ArrayList<>();
		list = compDb.getAllCompanies();

		if (!list.isEmpty()) {
			return compDb.getAllCompanies();
		}

		throw new ApplicationException(ErrorTypes.NO_COMPANIES);
	}
}

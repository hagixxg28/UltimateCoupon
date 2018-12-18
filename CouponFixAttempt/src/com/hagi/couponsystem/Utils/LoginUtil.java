package com.hagi.couponsystem.Utils;

import com.hagi.couponsystem.Enums.ClientType;
import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class LoginUtil {

	private static ICompanyDao compdb = CompanyDao.getInstance();
	private static ICustomerDao custdb = CustomerDao.getInstance();

	public static boolean logIn(long id, String password, ClientType type) throws ApplicationException {
		switch (type) {
		case CUSTOMER:
			return custdb.login(id, password);
		case COMPANY:
			return compdb.login(id, password);

		default:
			if (id == 1234 && password.equals("admin")) {
				return true;
			}
			return false;
		}
	}
}

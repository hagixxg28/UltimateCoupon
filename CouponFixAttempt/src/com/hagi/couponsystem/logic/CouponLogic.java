package com.hagi.couponsystem.logic;

import java.sql.Date;
import java.util.Collection;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Idao.ICouponDao;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CouponDao;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.exception.dao.DaoException;
import com.hagi.couponsystem.exception.facade.FacadeException;
import com.hagi.couponsystem.exception.facade.NoCouponsException;
import com.hagi.couponsystem.exception.facade.OutOfCouponsException;

public class CouponLogic {
	private ICouponDao coupDb = new CouponDao();
	private ICustomerDao custDb = new CustomerDao();
	private ICompanyDao compDb = new CompanyDao();

	public CouponLogic() {

	}

	public void createCoupon(Coupon coup) throws FacadeException {
		try {
			if (!compDb.companyExists(coup.getCompId())) {
				Collection<Coupon> List;
				List = coupDb.getAllCouponsForCompany(coup.getCompId());
				if (List.isEmpty()) {
					coupDb.createCoupon(coup);
					return;
				}
				for (Coupon coupon : List) {
					if (coupon.getTitle().equals(coup.getTitle())) {
						throw new FacadeException("A coupon with this title already exists");
					}
					coupDb.createCoupon(coup);
					return;
				}
			} else {
				throw new FacadeException("No company");
			}
		} catch (DaoException e) {
			throw new FacadeException("failed to create coupon");
		}
	}

	public void removeCoupon(Long coupId) throws FacadeException {
		try {
			coupDb.fullyRemoveCoupon(coupId);
		} catch (DaoException e) {
			throw new FacadeException("Coupon not found");
		}
	}

	public void updateCoupon(Coupon coup) throws FacadeException {
		try {
			coupDb.updateCoupon(coup);
		} catch (DaoException e) {
			throw new NoCouponsException("There is no such coupon in the Database");
		}

	}

	public Coupon getCoupon(Long coupId) throws FacadeException {
		try {
			return coupDb.getCoupon(coupId);
		} catch (DaoException e) {
			throw new NoCouponsException("There is no such coupon in the Database");
		}
	}

	public Collection<Coupon> getAllCouponForCompany(Long compId) throws FacadeException {
		try {
			if (compDb.companyExists(compId)) {
				return coupDb.getAllCouponsForCompany(compId);
			}
		} catch (DaoException e) {
			throw new FacadeException("We have encountered a probelm, try logging in again");
		}
		return null;
	}

	public Collection<Coupon> getAllCouponByTypeForcompany(CouponType type, Long compId) throws FacadeException {
		if (CouponType.typeValidator(type)) {
			try {
				if (compDb.companyExists(compId)) {
					return coupDb.getCouponByTypeForCompany(type, compId);
				} else {
					throw new FacadeException("Invalid company id, try logging in again");
				}

			} catch (DaoException e) {
				throw new FacadeException("We have encountered a probelm, try logging in again");
			}
		}
		return null;
	}

	public Collection<Coupon> getAllCouponByPriceForCompany(double price, Long compId) throws FacadeException {
		try {
			if (compDb.companyExists(compId)) {
				return coupDb.getCouponByPriceForCompany(price, compId);
			}
		} catch (DaoException e) {
			throw new FacadeException("We have encountered an error, try logging in");
		}
		return null;
	}

	public Collection<Coupon> getAllCouponByDateForCompany(Date date, Long compId) throws FacadeException {
		try {
			if (compDb.companyExists(compId)) {
				return coupDb.getCouponByDateForCompany(date, compId);
			}
		} catch (DaoException e) {
			throw new FacadeException("We have encountered an error, try logging in");
		}
		return null;
	}

	public Collection<Coupon> getAllCoupon() throws FacadeException {
		try {
			return coupDb.getAllCoupons();

		} catch (DaoException e) {
			throw new FacadeException("We have encountered a probelm, try logging in again");
		}
	}

	public Collection<Coupon> getAllCouponByType(CouponType type) throws FacadeException {
		if (CouponType.typeValidator(type)) {
			try {
				return coupDb.getCouponByType(type);
			} catch (DaoException e) {
				e.printStackTrace();
				throw new FacadeException("We have encountered a probelm, try logging in again");
			}
		}
		throw new FacadeException("You have entered an invalid type");
	}

	public Collection<Coupon> getAllCouponByPrice(double price) throws FacadeException {
		try {
			return coupDb.getCouponByPrice(price);
		} catch (DaoException e) {
			throw new FacadeException("We have encountered an error, try logging in");
		}
	}

	public Collection<Coupon> getAllCouponByDate(Date date) throws FacadeException {
		try {
			return coupDb.getCouponByDate(date);
		} catch (DaoException e) {
			throw new FacadeException("something went wrong");
		}
	}

	public void purchaseCoupon(Long couponId, Long customerId) throws FacadeException {
		Coupon coup = null;
		try {
			coup = coupDb.getCoupon(couponId);

		if (coup.getAmount() <= 0) {
			throw new OutOfCouponsException("There are no coupons left to buy");
		}
		} catch (DaoException e1) {
			throw new NoCouponsException("No coupon with this Id was found");
		}
		try {

			if (custDb.customerExists(customerId)) {

				Collection<Coupon> coupons = coupDb.getCouponsForCustomer(customerId);
				for (Coupon coupon : coupons) {
					if (coupon.getId() == coup.getId()) {
						throw new FacadeException("You already own this coupon");
					}
				}
				coupDb.customerPurchaseCoupon(couponId, customerId);
				coup.setAmount(coup.getAmount() - 1);
				coupDb.updateCoupon(coup);
			}

		} catch (DaoException e) {
			throw new FacadeException("You already own this coupon/Other exception");

		}
	}

	public Collection<Coupon> getAllCouponByTypeForCustomer(CouponType type, Long customerId) throws FacadeException {
		if (CouponType.typeValidator(type)) {
			try {
				if (custDb.customerExists(customerId)) {
					return coupDb.getCouponByTypeForCustomer(type, customerId);
				} else {
					throw new FacadeException("Invalid customer Id");
				}
			} catch (DaoException e) {
				throw new FacadeException("We have encountered a probelm, try logging in again");
			}
		}
		return null;
	}

	public Collection<Coupon> getAllCouponByPriceForCustomer(double price, Long customerId) throws FacadeException {
		try {
			if (custDb.customerExists(customerId)) {
				return coupDb.getCouponByPriceForCustomer(price, customerId);
			}
		} catch (DaoException e) {
			throw new FacadeException("Customer id is invalid");
		}
		return null;
	}

	public Collection<Coupon> getAllCouponByDateForCustomer(Date date, Long custId) throws FacadeException {
		try {
			if (custDb.customerExists(custId)) {
				return coupDb.getCouponByDateForCustomer(date, custId);
			}
		} catch (DaoException e) {
			throw new FacadeException("We have encountered an error, try logging in");
		}
		return null;
	}

	public Collection<Coupon> getAllCouponForCustomer(Long custId) throws FacadeException {
		try {
			if (custDb.customerExists(custId)) {
				return coupDb.getCouponsForCustomer(custId);
			}
		} catch (DaoException e) {
			throw new FacadeException("We have encountered a probelm, try logging in again");
		}
		return null;
	}
}
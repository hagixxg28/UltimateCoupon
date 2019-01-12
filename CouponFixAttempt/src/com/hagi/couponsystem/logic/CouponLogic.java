package com.hagi.couponsystem.logic;

import java.sql.Date;
import java.util.Collection;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Idao.ICouponDao;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.Utils.Validator;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CouponDao;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CouponLogic {
	private ICouponDao coupDb = CouponDao.getInstance();
	private ICustomerDao custDb = CustomerDao.getInstance();
	private ICompanyDao compDb = CompanyDao.getInstance();
	private static CouponLogic instance;

	private CouponLogic() {
		super();
	}

	public static CouponLogic getInstance() {
		if (instance == null) {
			instance = new CouponLogic();
		}
		return instance;
	}

	public void createCoupon(Coupon coup) throws ApplicationException {
		Validator.validateCoupon(coup);
		if (compDb.companyExists(coup.getCompId())) {
			if (!coupDb.companyHasCoupons(coup.getCompId())) {
				coupDb.createCoupon(coup);
				return;
			}

			Collection<Coupon> List;
			List = coupDb.getAllCouponsForCompany(coup.getCompId());

			for (Coupon coupon : List) {
				if (coupon.getTitle().equals(coup.getTitle())) {
					throw new ApplicationException(ErrorTypes.SAME_TITLE);
				}
			}

			coupDb.createCoupon(coup);
			return;
		}

		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public void removeCoupon(Long coupId) throws ApplicationException {

		if (coupDb.couponExists(coupId)) {
			coupDb.fullyRemoveCoupon(coupId);
			return;
		}
		throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
	}

	public void updateCoupon(Coupon coupon) throws ApplicationException {
		if (coupDb.couponExists(coupon.getId())) {
			Coupon updateCoupon = coupDb.getCoupon(coupon.getId());
			Validator.validateAndSetCoupon(coupon, updateCoupon);

			Collection<Coupon> List;
			List = coupDb.getAllCouponsForCompany(updateCoupon.getCompId());

			for (Coupon coup : List) {
				if (coup.getTitle().equals(coup.getTitle()) && coup.getId() != coup.getId()) {
					throw new ApplicationException(ErrorTypes.SAME_TITLE);
				}
			}
			coupDb.updateCoupon(updateCoupon);
			return;
		}
		throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);

	}

	public Coupon getCoupon(Long coupId) throws ApplicationException {
		if (coupDb.couponExists(coupId)) {
			return coupDb.getCoupon(coupId);
		}
		throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
	}

	public Collection<Coupon> getAllCouponForCompany(Long compId) throws ApplicationException {
		if (compDb.companyExists(compId)) {
			if (coupDb.companyHasCoupons(compId)) {
				return coupDb.getAllCouponsForCompany(compId);
			}
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public Collection<Coupon> getAllCouponByTypeForcompany(CouponType type, Long compId) throws ApplicationException {
		if (CouponType.typeValidator(type)) {
			if (compDb.companyExists(compId)) {
				Collection<Coupon> list = coupDb.getCouponByTypeForCompany(type, compId);
				Validator.validateCollectionCoupon(list);
				return list;
			}
			throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
		}
		throw new ApplicationException(ErrorTypes.INVALID_TYPE);
	}

	public Collection<Coupon> getAllCouponByPriceForCompany(double price, Long compId) throws ApplicationException {
		Validator.priceValidator(price);
		if (compDb.companyExists(compId)) {
			Collection<Coupon> list = coupDb.getCouponByPriceForCompany(price, compId);
			Validator.validateCollectionCoupon(list);
			return list;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public Collection<Coupon> getAllCouponByDateForCompany(Date date, Long compId) throws ApplicationException {
		Validator.endDateValidaotr(date);
		if (compDb.companyExists(compId)) {
			Collection<Coupon> list = coupDb.getCouponByDateForCompany(date, compId);
			Validator.validateCollectionCoupon(list);
			return list;
		}
		throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
	}

	public Collection<Coupon> getAllCoupon() throws ApplicationException {
		Collection<Coupon> list = coupDb.getAllCoupons();
		Validator.validateCollectionCoupon(list);
		// This makes sure not to return coupons that are sold out but still held by
		// users
		Validator.amountValidatorAndCollectionCleaner(list);
		return list;
	}

	public Collection<Coupon> getAllCouponByType(CouponType type) throws ApplicationException {
		if (CouponType.typeValidator(type)) {
			Collection<Coupon> list = coupDb.getCouponByType(type);
			Validator.validateCollectionCoupon(list);
			Validator.amountValidatorAndCollectionCleaner(list);
			return list;
		}
		throw new ApplicationException(ErrorTypes.INVALID_TYPE);
	}

	public Collection<Coupon> getAllCouponByPrice(double price) throws ApplicationException {
		Validator.priceValidator(price);
		Collection<Coupon> list = coupDb.getCouponByPrice(price);
		Validator.validateCollectionCoupon(list);
		Validator.amountValidatorAndCollectionCleaner(list);
		return list;
	}

	public Collection<Coupon> getAllCouponByDate(Date date) throws ApplicationException {
		Validator.endDateValidaotr(date);
		Collection<Coupon> list = coupDb.getCouponByDate(date);
		Validator.validateCollectionCoupon(list);
		Validator.amountValidatorAndCollectionCleaner(list);
		return list;
	}

	public void purchaseCoupon(Long couponId, Long customerId) throws ApplicationException {
		Coupon coup = null;
		if (coupDb.couponExists(couponId)) {

			coup = coupDb.getCoupon(couponId);

			if (coup.getAmount() <= 0) {
				throw new ApplicationException(ErrorTypes.OUT_OF_COUPONS);
			}

			if (custDb.customerExists(customerId)) {
				if (coupDb.customerHasCoupons(customerId)) {
					Collection<Coupon> coupons = coupDb.getCouponsForCustomer(customerId);

					for (Coupon coupon : coupons) {
						if (coupon.getId() == coup.getId()) {
							throw new ApplicationException(ErrorTypes.CUSTOMER_OWNS_COUPON);
						}
					}
				}
				coupDb.customerPurchaseCoupon(couponId, customerId);
				coup.setAmount(coup.getAmount() - 1);
				coupDb.updateCoupon(coup);
				return;
			}
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
		}
		throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);

	}

	public Collection<Coupon> getAllCouponByTypeForCustomer(CouponType type, Long customerId)
			throws ApplicationException {
		if (CouponType.typeValidator(type)) {
			if (custDb.customerExists(customerId)) {

				Collection<Coupon> list = coupDb.getCouponByTypeForCustomer(type, customerId);
				Validator.validateCollectionCoupon(list);
				return list;
			}

			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
		}
		throw new ApplicationException(ErrorTypes.INVALID_TYPE);
	}

	public Collection<Coupon> getAllCouponByPriceForCustomer(double price, Long customerId)
			throws ApplicationException {

		Validator.priceValidator(price);
		if (custDb.customerExists(customerId)) {
			Collection<Coupon> list = coupDb.getCouponByPriceForCustomer(price, customerId);
			Validator.validateCollectionCoupon(list);
			return list;
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}

	public Collection<Coupon> getAllCouponByDateForCustomer(Date date, Long custId) throws ApplicationException {
		Validator.endDateValidaotr(date);
		if (custDb.customerExists(custId)) {
			Collection<Coupon> list = coupDb.getCouponByDateForCustomer(date, custId);
			Validator.validateCollectionCoupon(list);
			return list;
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}

	public Collection<Coupon> getAllCouponForCustomer(Long custId) throws ApplicationException {
		if (custDb.customerExists(custId)) {
			if (coupDb.customerHasCoupons(custId)) {
				return coupDb.getCouponsForCustomer(custId);
			}
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		}
		throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
	}
}
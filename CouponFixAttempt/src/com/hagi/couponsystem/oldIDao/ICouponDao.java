package com.hagi.couponsystem.oldIDao;

import java.sql.Date;
import java.util.Collection;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.exceptions.ApplicationException;

public interface ICouponDao {

	void createCoupon(Coupon coup) throws ApplicationException;

	void customerPurchaseCoupon(Long coupId, Long custId) throws ApplicationException;

	void fullyRemoveCoupon(Long id) throws ApplicationException;

	void removeCouponCust(Long id) throws ApplicationException;

	void removeCouponCoup(Long id) throws ApplicationException;

	void updateCoupon(Coupon coup) throws ApplicationException;
	
	boolean couponExists(Long id) throws ApplicationException;
	
	boolean companyHasCoupons(Long compId) throws ApplicationException;
	
	boolean customerHasCoupons(Long custId) throws ApplicationException;

	Coupon getCoupon(Long id) throws ApplicationException;

	Collection<Coupon> getAllCouponsForCompany(Long id) throws ApplicationException;

	Collection<Coupon> getCouponsForCustomer(Long id) throws ApplicationException;

	Collection<Coupon> getAllCoupons() throws ApplicationException;

	Collection<Long> getAllExpiredCoupons() throws ApplicationException;

	Collection<Coupon> getCouponByType(CouponType type) throws ApplicationException;

	Collection<Coupon> getCouponByTypeForCustomer(CouponType type, long customerId) throws ApplicationException;

	Collection<Coupon> getCouponByTypeForCompany(CouponType type, long companyId) throws ApplicationException;

	Collection<Coupon> getCouponByPrice(double price) throws ApplicationException;

	Collection<Coupon> getCouponByPriceForCompany(double price, long companyId) throws ApplicationException;

	Collection<Coupon> getCouponByPriceForCustomer(double price, long customerId) throws ApplicationException;

	Collection<Coupon> getCouponByDateForCustomer(Date date, long custId) throws ApplicationException;

	Collection<Coupon> getCouponByDateForCompany(Date date, long compId) throws ApplicationException;

	Collection<Coupon> getCouponByDate(Date date) throws ApplicationException;
}
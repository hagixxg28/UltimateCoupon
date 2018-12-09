package com.hagi.couponsystem.Idao;

import java.sql.Date;
import java.util.Collection;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.exception.dao.DaoException;

public interface ICouponDao {

	void createCoupon(Coupon coup) throws DaoException;

	void customerPurchaseCoupon(Long coupId, Long custId) throws DaoException;

	void fullyRemoveCoupon(Long id) throws DaoException;

	void removeCouponCust(Long id) throws DaoException;

	void removeCouponCoup(Long id) throws DaoException;

	void updateCoupon(Coupon coup) throws DaoException;

	Coupon getCoupon(Long id) throws DaoException;

	Collection<Coupon> getAllCouponsForCompany(Long id) throws DaoException;

	Collection<Coupon> getCouponsForCustomer(Long id) throws DaoException;

	Collection<Coupon> getAllCoupons() throws DaoException;

	Collection<Long> getAllExpiredCoupons() throws DaoException;

	Collection<Coupon> getCouponByType(CouponType type) throws DaoException;

	Collection<Coupon> getCouponByTypeForCustomer(CouponType type, long customerId) throws DaoException;

	Collection<Coupon> getCouponByTypeForCompany(CouponType type, long companyId) throws DaoException;

	Collection<Coupon> getCouponByPrice(double price) throws DaoException;

	Collection<Coupon> getCouponByPriceForCompany(double price, long companyId) throws DaoException;

	Collection<Coupon> getCouponByPriceForCustomer(double price, long customerId) throws DaoException;

	Collection<Coupon> getCouponByDateForCustomer(Date date, long custId) throws DaoException;

	Collection<Coupon> getCouponByDateForCompany(Date date, long compId) throws DaoException;

	Collection<Coupon> getCouponByDate(Date date) throws DaoException;
}
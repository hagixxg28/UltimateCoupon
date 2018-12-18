package com.hagi.couponsystem.tests;

import java.sql.Date;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CouponDao;
import com.hagi.couponsystem.exceptions.ApplicationException;
import com.hagi.couponsystem.logic.CouponLogic;

public class aaaa {
	public static void main(String[] args) {

		Coupon coupon = new Coupon();
		CompanyDao compDb = CompanyDao.getInstance();
		CouponDao coupDb = CouponDao.getInstance();
		coupon.setId(1234);
		coupon.setAmount(5);
		coupon.setCompId(12343);
		coupon.setEndDate(new Date(System.currentTimeMillis()));
		coupon.setImage("aassd");
		coupon.setMessage("heoi");
		coupon.setPrice(55);
		coupon.setStartDate(new Date(System.currentTimeMillis()));
		coupon.setTitle("test");
		coupon.setType(CouponType.ELECTRICITY);

		CouponLogic logic = CouponLogic.getInstance();
		String enums = CouponType.typeToString(coupon.getType());
		CouponType typeparse = CouponType.typeSort(enums);

		Date date = new Date(9999);
		System.out.println(date);
		try {
			System.out.println(compDb.companyExists((long) 2311));
			System.out.println(coupDb.getCouponByTypeForCompany(typeparse, 2311));
		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("--------------------------------");
		try {
			System.out.println(logic.getCoupon(coupon.getId()));
			System.out.println(logic.getAllCouponByDate(date));
			System.out.println(coupon.getType());
			System.out.println(enums);
			System.out.println(logic.getAllCoupon());
			System.out.println(logic.getAllCouponByType(coupon.getType()));
			System.out.println(typeparse);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
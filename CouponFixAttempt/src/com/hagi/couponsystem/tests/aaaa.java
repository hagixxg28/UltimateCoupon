package com.hagi.couponsystem.tests;

import java.sql.Date;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.exception.facade.FacadeException;
import com.hagi.couponsystem.logic.CouponLogic;

public class aaaa {
	public static void main(String[] args) {

		Coupon coupon = new Coupon();

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

		CouponLogic logic = new CouponLogic();

		try {
			System.out.println(logic.getAllCoupon());
			System.out.println(logic.getAllCouponByType(coupon.getType()));
		} catch (FacadeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
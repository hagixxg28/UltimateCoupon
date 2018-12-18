package com.hagi.couponsystem.thread;

import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Idao.ICouponDao;
import com.hagi.couponsystem.dao.CouponDao;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class DailyCouponExpirationTask implements Runnable {

	private ICouponDao coupDb = CouponDao.getInstance();
	private boolean quit = false;

	public DailyCouponExpirationTask() {
	}

	@Override
	public void run() {
		while (!quit) {
			Collection<Long> list = new ArrayList<>();
			try {

				list = coupDb.getAllExpiredCoupons();
				if (!list.isEmpty()) {
					for (Long long1 : list) {
						coupDb.fullyRemoveCoupon(long1);
					}
				}
				try {
					Thread.sleep(86400000);

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		quit = true;
		Thread.currentThread().interrupt();
	}

}

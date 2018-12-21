package com.hagi.couponsystem.coupsys;

import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.dao.CompanyDao;
import com.hagi.couponsystem.dao.CustomerDao;
import com.hagi.couponsystem.thread.DailyCouponExpirationTask;

public class CouponSystem {
	@SuppressWarnings("unused")
	private ICompanyDao compDb;
	@SuppressWarnings("unused")
	private ICustomerDao custDb;
	private static CouponSystem coupSys = null;
	private DailyCouponExpirationTask task = new DailyCouponExpirationTask();
	private Thread thread = new Thread(task);

	private CouponSystem() {
		System.out.println("got coup sys");
		ConnectionPool.getPool();
		compDb = CompanyDao.getInstance();
		custDb = CustomerDao.getInstance();
		thread.start();
	}

	public static CouponSystem getInstance() {
		if (coupSys == null) {
			coupSys = new CouponSystem();
			return coupSys;
		} else {
			return coupSys;
		}
	}

	public void shutDown() {
		task.stop();
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionPool.getPool().closeConnections();
	}
}

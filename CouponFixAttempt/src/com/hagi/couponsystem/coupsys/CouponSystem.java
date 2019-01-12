package com.hagi.couponsystem.coupsys;

import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.thread.DailyCouponExpirationTask;

public class CouponSystem {
	// Eager Singletone
	private static CouponSystem coupSys = new CouponSystem();
	private DailyCouponExpirationTask task = new DailyCouponExpirationTask();
	private Thread thread = new Thread(task);

	private CouponSystem() {
		System.out.println("got coup sys");
		ConnectionPool.getPool();
		thread.start();
	}

	public static CouponSystem getInstance() {
		return coupSys;
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

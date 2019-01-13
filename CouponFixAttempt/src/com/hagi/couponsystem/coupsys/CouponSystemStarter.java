package com.hagi.couponsystem.coupsys;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class CouponSystemStarter
 *
 */
@WebListener
public class CouponSystemStarter implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public CouponSystemStarter() {
		CouponSystem.getInstance();

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		CouponSystem instance = CouponSystem.getInstance();
		instance.shutDown();

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}

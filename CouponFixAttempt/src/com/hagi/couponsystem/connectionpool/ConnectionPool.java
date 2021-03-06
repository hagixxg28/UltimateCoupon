package com.hagi.couponsystem.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {

	private Stack<Connection> conStack = new Stack<>();
	// Eager Singletone
	private static ConnectionPool instance = new ConnectionPool();
	public static final int MAX = 10;
	String driverName = "org.apache.derby.jdbc.ClientDriver";
	String url = "jdbc:derby://localhost:1527/coupons_database";

//	String driverName = "com.mysql.cj.jdbc.Driver";
//	String url = "jdbc:mysql://localhost:3306/coupons?user=root&password=1234&serverTimezone=UTC";

	private ConnectionPool() {
		super();
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
//			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < MAX; i++) {
			try {
				Connection con = DriverManager.getConnection(url);
				conStack.push(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static ConnectionPool getPool() {
		// Eager Singletone
		return instance;
	}

	public synchronized Connection getConnection() {
		while (conStack.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Connection con = null;
		con = conStack.pop();
		return con;
	}

	public synchronized void returnConnection(Connection Connection) {
		conStack.push(Connection);
		notifyAll();
	}

	public synchronized void closeConnections() {
		int counter = 0;
		while (counter != MAX) {
			for (Connection connection : conStack) {
				try {
					connection.close();
					counter++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			conStack.removeAll(conStack);
			if (counter == MAX) {
				return;
			}
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

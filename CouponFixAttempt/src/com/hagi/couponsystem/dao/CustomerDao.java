package com.hagi.couponsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.Utils.Extractor;
import com.hagi.couponsystem.Utils.UtilSQLcloser;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CustomerDao implements ICustomerDao {
	private ConnectionPool pool = ConnectionPool.getPool();
	private static CustomerDao instance;

	private CustomerDao() {
		super();
	}

	public static CustomerDao getInstance() {
		if (instance == null) {
			instance = new CustomerDao();
		}
		return instance;
	}

	@Override
	public void createCustomer(Customer cust) throws ApplicationException {
		String sql = "INSERT INTO customer  (cust_id,name,password) VALUES (?,?,?)";
		Connection connection = pool.getConnection();
		try (PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setLong(1, cust.getId());
			stmt.setString(2, cust.getcustName());
			stmt.setString(3, cust.getPassword());
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_ALREADY_EXISTS);

		} finally {
			pool.returnConnection(connection);
		}

	}

	@Override
	public void removeCustomer(Long id) throws ApplicationException {
		String sql = String.format("DELETE FROM customer WHERE cust_id=%d", id);
		Connection connection = pool.getConnection();
		try (PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);

		} finally {
			pool.returnConnection(connection);
		}

	}

	@Override
	public void updateCustomer(Customer cust) throws ApplicationException {
		String sql = "UPDATE customer SET name=?, password=? WHERE cust_id=?";
		Connection connection = pool.getConnection();
		try (PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, cust.getcustName());
			stmt.setString(2, cust.getPassword());
			stmt.setLong(3, cust.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);

		} finally {
			pool.returnConnection(connection);
		}

	}

	@Override
	public Customer getCustomer(Long id) throws ApplicationException {
		Customer customer = null;
		String sql = "SELECT * FROM customer WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				customer = Extractor.extractCustomerFromResultSet(resultSet);
			}
			return customer;
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Customer> getAllCustomer() throws ApplicationException {
		Collection<Customer> collection = new ArrayList<Customer>();
		String sql = "SELECT * FROM customer";
		Connection connection = pool.getConnection();
		try (Statement stmt = connection.createStatement(); ResultSet resultSet = stmt.executeQuery(sql);) {

			while (resultSet.next()) {
				Customer customer = null;
				customer = Extractor.extractCustomerFromResultSet(resultSet);
				collection.add(customer);
			}
			return collection;
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_CUSTOMERS);
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public Boolean login(Long id, String password) throws ApplicationException {
		String sql = "SELECT password FROM customer WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			String str = resultSet.getString("password");
			if (password.equals(str)) {
				return true;
			}
			return false;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.FAILED_TO_LOGIN);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Boolean customerExists(Long id) throws ApplicationException {
		String sql = "SELECT cust_id FROM customer WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();

			return resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}
}

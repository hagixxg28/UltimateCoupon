package com.hagi.couponsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Idao.ICustomerDao;
import com.hagi.couponsystem.Utils.Extractor;
import com.hagi.couponsystem.Utils.UtilSQLcloser;
import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.exception.dao.CustomerAlreadyExistsException;
import com.hagi.couponsystem.exception.dao.CustomerDoesNotExistException;
import com.hagi.couponsystem.exception.dao.DaoException;
import com.hagi.couponsystem.exception.dao.NoCustomersException;

public class CustomerDao implements ICustomerDao {
	private ConnectionPool pool = ConnectionPool.getPool();

	public CustomerDao() {
		super();
	}

	@Override
	public void createCustomer(Customer cust) throws DaoException {
		String sql = "INSERT INTO customer  (cust_id,name,password) VALUES (?,?,?)";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setLong(1, cust.getId());
			stmt.setString(2, cust.getCustName());
			stmt.setString(3, cust.getPassword());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new CustomerAlreadyExistsException("This customer already exists");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void removeCustomer(Long id) throws DaoException {
		String sql = String.format("DELETE FROM customer WHERE cust_id=%d", id);
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("This customer does not exist");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void updateCustomer(Customer cust) throws DaoException {
		String sql = "UPDATE customer SET name=?, password=? WHERE cust_id=?";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, cust.getCustName());
			stmt.setString(2, cust.getPassword());
			stmt.setLong(3, cust.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("This customer does not exist");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Customer getCustomer(Long id) throws DaoException {
		Customer customer = new Customer();
		String sql = String.format("SELECT * FROM customer WHERE cust_id=%d", id);
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet resultSet = stmt.executeQuery();) {
			while (resultSet.next()) {
				customer = Extractor.extractCustomerFromResultSet(resultSet);
//				customer.setCoupons(getCoupons(customer));
			}
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("This customer does not exist");
		} finally {
			pool.returnConnection(con);
		}
		return customer;

	}

	@Override
	public Collection<Customer> getAllCustomer() throws DaoException {
		Collection<Customer> collection = new ArrayList<Customer>();
		String sql = "SELECT * FROM customer";
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet resultSet = stmt.executeQuery(sql);) {

			while (resultSet.next()) {
				Customer customer = null;
				customer = Extractor.extractCustomerFromResultSet(resultSet);
				collection.add(customer);
			}
		} catch (SQLException e) {
			throw new NoCustomersException("There are no customers at the Database");
		} finally {
			pool.returnConnection(con);
		}
		return collection;

	}

//	@Override
//	public Collection<Customer> getAllCustomerWithCoupons() throws DaoException {
//		Collection<Customer> collection = new ArrayList<Customer>();
//		String sql = "SELECT * FROM customer";
//		Connection con = pool.getConnection();
//		try (Statement stmt = con.createStatement(); ResultSet resultSet = stmt.executeQuery(sql);) {
//			while (resultSet.next()) {
//				Customer customer = null;
//				customer = extractor.extractCustomerFromResultSet(resultSet);
//				customer.setCoupons(getCoupons(customer));
//				if (!customer.getCoupons().isEmpty()) {
//					collection.add(customer);
//				}
//			}
//		} catch (SQLException e) {
//			throw new NoCustomersException("There are no customers");
//		} finally {
//			pool.returnConnection(con);
//		}
//		return collection;
//
//	}

	@Override
	public Boolean login(Long id, String password) throws DaoException {
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
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("No customer with this id found");
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public Boolean customerExists(Long id) throws DaoException {
		ArrayList<Long> list = new ArrayList<>();
		String sql = "SELECT cust_id FROM customer WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				list.add((resultSet.getLong("cust_id")));
			}
			for (Long long1 : list) {
				if (id.equals(long1)) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("This customer does not exist");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return false;
	}
}

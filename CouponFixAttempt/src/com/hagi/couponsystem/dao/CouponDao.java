package com.hagi.couponsystem.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.Idao.ICouponDao;
import com.hagi.couponsystem.Utils.Extractor;
import com.hagi.couponsystem.Utils.UtilSQLcloser;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.exception.dao.CouponAlreadyExistsException;
import com.hagi.couponsystem.exception.dao.CouponDoesNotExistException;
import com.hagi.couponsystem.exception.dao.CustomerDoesNotExistException;
import com.hagi.couponsystem.exception.dao.DaoException;

public class CouponDao implements ICouponDao {
	private ConnectionPool pool = ConnectionPool.getPool();

	public CouponDao() {
		super();
	}

	@Override
	public void createCoupon(Coupon coup) throws DaoException {
		String sql = "INSERT INTO coupon VALUES (?, ?, ?, ? ,?, ?, ?, ?, ?, ?)";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setLong(1, coup.getId());
			stmt.setString(2, coup.getTitle());
			stmt.setDate(3, coup.getStartDate());
			stmt.setDate(4, coup.getEndDate());
			stmt.setInt(5, coup.getAmount());
			stmt.setString(6, CouponType.typeToString(coup.getType()));
			stmt.setString(7, coup.getMessage());
			stmt.setDouble(8, coup.getPrice());
			stmt.setString(9, coup.getImage());
			stmt.setLong(10, coup.getCompId());
			stmt.executeUpdate();
			System.out.println(coup + " has been added");
		} catch (SQLException e) {
			throw new CouponAlreadyExistsException("This coupon already exists");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void fullyRemoveCoupon(Long id) throws DaoException {
		String sql = String.format("DELETE FROM coupon WHERE coup_id=%d", id);
		String sql3 = String.format("DELETE FROM customer_coupon WHERE coup_id=%d", id);
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);
				PreparedStatement stmt3 = con.prepareStatement(sql3);) {
			stmt.executeUpdate();
			stmt3.executeUpdate();
			System.out.println("Coupon with id " + id + " has been removed");
		} catch (SQLException e) {
			throw new CouponDoesNotExistException("This coupon does not exist");
		} finally {
			pool.returnConnection(con);
		}
	}

	@Override
	public void updateCoupon(Coupon coup) throws DaoException {
		String sql = "UPDATE coupon SET title=?, start_date=?, end_date=?, amount=?, type=?, message=?, price=?, image=?, comp_id=? WHERE coup_id=?";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, coup.getTitle());
			stmt.setDate(2, coup.getStartDate());
			stmt.setDate(3, coup.getEndDate());
			stmt.setInt(4, coup.getAmount());
			stmt.setString(5, CouponType.typeToString(coup.getType()));
			stmt.setString(6, coup.getMessage());
			stmt.setDouble(7, coup.getPrice());
			stmt.setString(8, coup.getImage());
			stmt.setLong(9, coup.getCompId());
			stmt.setLong(10, coup.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponDoesNotExistException("This coupon does not exist");
		} finally {
			pool.returnConnection(con);
		}
	}

	@Override
	public Coupon getCoupon(Long id) throws DaoException {
		Coupon coupon = new Coupon();
		String sql = "SELECT * FROM coupon WHERE coup_id=" + id;
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet resultSet = stmt.executeQuery();) {
			while (resultSet.next()) {
				coupon = Extractor.extractCouponFromResultSet(resultSet);
			}

		} catch (SQLException e) {
			throw new CouponDoesNotExistException("This coupon does not exist");
		} finally {
			pool.returnConnection(con);
		}
		return coupon;
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon";
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet resultSet = stmt.executeQuery(sql);) {
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}
		} catch (SQLException e) {
			throw new CouponDoesNotExistException("There are no coupons at the database");
		} finally {
			pool.returnConnection(con);
		}
		if (!collection.isEmpty()) {
			return collection;
		} else {
			throw new CouponDoesNotExistException("There are no coupons at the database");
		}
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE type=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, CouponType.typeToString(type));
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}
		} catch (SQLException e) {
			throw new CouponDoesNotExistException("There are no coupons with that type at the database");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return collection;
	}

	@Override
	public void customerPurchaseCoupon(Long coupId, Long custId) throws DaoException {
		String sql2 = "INSERT INTO customer_coupon VALUES(?, ?)";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt2 = con.prepareStatement(sql2);) {
			stmt2.setLong(1, custId);
			stmt2.setLong(2, coupId);
			stmt2.executeUpdate();
		} catch (SQLException e) {
			throw new CouponAlreadyExistsException("This coupon already exists for this customer");
		}
	}

	@Override
	public void removeCouponCust(Long id) throws DaoException {
		String sql2 = String.format("DELETE FROM customer_coupon WHERE coup_id=%d", id);
		Connection con = pool.getConnection();
		try (PreparedStatement stmt2 = con.prepareStatement(sql2);) {
			stmt2.executeUpdate();
			System.out.println("Coupon with id " + id + " has been removed");
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("This customer does not exist");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void removeCouponCoup(Long id) throws DaoException {
		String sql = String.format("DELETE FROM coupon WHERE coup_id=%d", id);
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponDoesNotExistException("This cuopon does not exist");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Collection<Long> getAllExpiredCoupons() throws DaoException {
		ArrayList<Long> list = new ArrayList<>();
		Date date1 = new Date(System.currentTimeMillis());
		String sql = "SELECT coup_id FROM coupon WHERE end_date < '" + date1 + "'";
		Connection con = ConnectionPool.getPool().getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				list.add(rs.getLong("coup_id"));
			}
		} catch (SQLException e) {
			throw new DaoException("An error has occoured");
		} finally {
			pool.returnConnection(con);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getAllCouponsForCompany(Long compId) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE comp_id=?";
//		String sql = "SELECT company_coupon.coup_id FROM company_coupon INNER JOIN coupon"
//				+ " ON coupon.coup_id=company_coupon.coup_id" + " WHERE comp_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		connection = pool.getConnection();

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, compId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}

		} catch (SQLException e) {
			throw new DaoException("No coupons were found for this Id");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponsForCustomer(Long id) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT customer_coupon.coup_id FROM customer_coupon INNER JOIN coupon"
				+ " ON coupon.coup_id=customer_coupon.coup_id" + " WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon other = new Coupon();
				other.setId(resultSet.getLong("coup_id"));
				collection.add(other);
			}
			String sql2 = "Select *  FROM coupon where coup_id=?";
			for (Coupon coupon : collection) {
				try {
					preparedStatement = connection.prepareStatement(sql2);
					preparedStatement.setLong(1, coupon.getId());
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						coupon = Extractor.extractCouponFromResultSet(resultSet);
					}
				} finally {
					UtilSQLcloser.SQLCloser(preparedStatement);
					pool.returnConnection(connection);
				}
			}
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("Couldn't find a customer with this coupon id");
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByPrice(double price) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE price<=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, price);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}

		} catch (SQLException e) {
			throw new DaoException("No coupons were found for this Id");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByPriceForCompany(double price, long companyId) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE price<=? AND comp_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, price);
			preparedStatement.setLong(2, companyId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}

		} catch (SQLException e) {
			throw new DaoException("No coupons were found for this Id");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByPriceForCustomer(double price, long customerId) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT customer_coupon.coup_id FROM customer_coupon INNER JOIN coupon"
				+ " ON coupon.coup_id=customer_coupon.coup_id" + " WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon other = new Coupon();
				other.setId(resultSet.getLong("coup_id"));
				collection.add(other);
			}
			String sql2 = "Select *  FROM coupon where coup_id=? AND price<=?";
			preparedStatement = connection.prepareStatement(sql2);
			for (Coupon coupon : collection) {
				try {
					preparedStatement.setLong(1, coupon.getId());
					preparedStatement.setDouble(2, price);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						coupon = Extractor.extractCouponFromResultSet(resultSet);
					}
				} finally {
					UtilSQLcloser.SQLCloser(preparedStatement);
					pool.returnConnection(connection);
				}
			}
		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("Couldn't find a customer with this coupon id");
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByDateForCustomer(Date date, long custId) throws DaoException {
		String sql = "SELECT coup_id FROM customer_coupon WHERE cust_id=?";
		Collection<Coupon> list = new ArrayList<Coupon>();
		Collection<Long> collection = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		connection = pool.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, custId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				collection.add(resultSet.getLong("coup_id"));
			}
			String sql2 = "SELECT * FROM coupon WHERE coup_id=? AND end_date >= '?'";
			for (Long long1 : collection) {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setLong(1, long1);
				preparedStatement.setDate(2, date);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Coupon coupon = null;
					coupon = Extractor.extractCouponFromResultSet(resultSet);
					list.add(coupon);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Something");

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getCouponByDateForCompany(Date date, long compId) throws DaoException {
		String sql = "SELECT * FROM coupon WHERE end_date >='?' AND comp_id=?";
		Collection<Coupon> list = new ArrayList<Coupon>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		connection = pool.getConnection();

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, date);
			preparedStatement.setLong(2, compId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				list.add(coupon);
			}
		} catch (SQLException e) {
			throw new DaoException("Something");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getCouponByDate(Date date) throws DaoException {
		String sql = "SELECT * FROM coupon WHERE end_date >='?'";
		Collection<Coupon> list = new ArrayList<Coupon>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		connection = pool.getConnection();

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, date);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				list.add(coupon);
			}
		} catch (SQLException e) {
			throw new DaoException("Something");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getCouponByTypeForCustomer(CouponType type, long customerId) throws DaoException {
		String sql = "SELECT coup_id FROM customer_coupon WHERE cust_id=?";
		Collection<Coupon> list = new ArrayList<Coupon>();
		Collection<Long> collection = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		connection = pool.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				collection.add(resultSet.getLong("coup_id"));
			}
			String sql2 = "SELECT * FROM coupon WHERE coup_id=? AND type= '?'";
			for (Long long1 : collection) {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setLong(1, long1);
				preparedStatement.setString(2, CouponType.typeToString(type));
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Coupon coupon = null;
					coupon = Extractor.extractCouponFromResultSet(resultSet);
					list.add(coupon);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Something");

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getCouponByTypeForCompany(CouponType type, long companyId) throws DaoException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE type='?' AND comp_id=? ";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, CouponType.typeToString(type));
			preparedStatement.setLong(2, companyId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}
		} catch (SQLException e) {
			throw new CouponDoesNotExistException("There are no coupons with that type at the database");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		if (!collection.isEmpty()) {
			return collection;
		} else {
			throw new CouponDoesNotExistException("There are no coupons with that type");
		}
	}

	// NOT USED AT THE MOMENT
	// public boolean customerOwnsCoupon(Coupon coup, Customer cust) throws
	// DaoException {
	// String sql2 = "SELECT * customer_coupon WHERE cust_id=" + cust.getId() +
	// " AND coup_id=" + coup.getId();
	// Connection con = pool.getConnection();
	// Collection<Object> collection = new ArrayList<>();
	// try (PreparedStatement stmt2 = con.prepareStatement(sql2); ResultSet rs =
	// stmt2.executeQuery();) {
	// while (rs.next()) {
	// rs.getLong("cust_id");
	// rs.getLong("coup_id");
	// }
	// if (collection.isEmpty()) {
	// System.out.println("passed own check");
	// return false;
	// } else {
	// System.out.println("failed own check");
	// return true;
	// }
	// } catch (SQLException e) {
	// throw new CouponAlreadyExistsException("No coupons were found");
	// }
	// }

}

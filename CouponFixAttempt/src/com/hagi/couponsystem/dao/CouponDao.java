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
import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICouponDao;
import com.hagi.couponsystem.Utils.Extractor;
import com.hagi.couponsystem.Utils.UtilSQLcloser;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CouponDao implements ICouponDao {
	private ConnectionPool pool = ConnectionPool.getPool();
	private static CouponDao instance;

	private CouponDao() {
		super();
	}

	public static CouponDao getInstance() {
		if (instance == null) {
			instance = new CouponDao();
		}
		return instance;
	}

	@Override
	public void createCoupon(Coupon coup) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.COUPON_ALREADY_EXISTS);
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void fullyRemoveCoupon(Long id) throws ApplicationException {
		String sql = "DELETE FROM coupon WHERE coup_id=?";
		String sql2 = "DELETE FROM customer_coupon WHERE coup_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
			preparedStatement2 = connection.prepareStatement(sql2);
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			UtilSQLcloser.SQLCloser(preparedStatement2);
			pool.returnConnection(connection);
		}
	}

	@Override
	public void updateCoupon(Coupon coup) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
		} finally {
			pool.returnConnection(con);
		}
	}

	@Override
	public Coupon getCoupon(Long id) throws ApplicationException {
		Coupon coupon = null;
		String sql = "SELECT * FROM coupon WHERE coup_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				return coupon;
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
		}
		throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws ApplicationException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon";
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet resultSet = stmt.executeQuery(sql);) {
			while (resultSet.next()) {
				Coupon coupon = null;
				coupon = Extractor.extractCouponFromResultSet(resultSet);
				collection.add(coupon);
			}
			if (!collection.isEmpty()) {
				return collection;
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			pool.returnConnection(con);
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws ApplicationException {
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
			if (!collection.isEmpty()) {
				return collection;
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public void customerPurchaseCoupon(Long coupId, Long custId) throws ApplicationException {
		String sql = "INSERT INTO customer_coupon VALUES(?, ?)";
		Connection connection = pool.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setLong(1, custId);
			preparedStatement.setLong(2, coupId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_OWNS_COUPON);
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public void removeCouponCust(Long id) throws ApplicationException {
		String sql = "DELETE FROM customer_coupon WHERE coup_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);
		} finally {
			pool.returnConnection(connection);
			UtilSQLcloser.SQLCloser(preparedStatement);
		}

	}

	@Override
	public void removeCouponCoup(Long id) throws ApplicationException {
		String sql = "DELETE FROM coupon WHERE coup_id=?";
		Connection con = pool.getConnection();
		try (PreparedStatement preparedStatement = con.prepareStatement(sql);) {
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Collection<Long> getAllExpiredCoupons() throws ApplicationException {
		ArrayList<Long> list = new ArrayList<>();
		Date date1 = new Date(System.currentTimeMillis());
		String sql = "SELECT coup_id FROM coupon WHERE end_date < '" + date1 + "'";
		Connection con = ConnectionPool.getPool().getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				list.add(rs.getLong("coup_id"));
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.SYSTEM_FAILFURE);
		} finally {
			pool.returnConnection(con);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getAllCouponsForCompany(Long compId) throws ApplicationException {
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
			if (!collection.isEmpty()) {
				return collection;
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public Collection<Coupon> getCouponsForCustomer(Long id) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByPrice(double price) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByPriceForCompany(double price, long companyId) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByPriceForCustomer(double price, long customerId) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		}
		return collection;
	}

	@Override
	public Collection<Coupon> getCouponByDateForCustomer(Date date, long custId) throws ApplicationException {
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
			String sql2 = "SELECT * FROM coupon WHERE coup_id=? AND end_date >= ?";
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
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getCouponByDateForCompany(Date date, long compId) throws ApplicationException {
		String sql = "SELECT * FROM coupon WHERE end_date >=? AND comp_id=?";
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
			if (!list.isEmpty()) {
				return list;
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public Collection<Coupon> getCouponByDate(Date date) throws ApplicationException {
		String sql = "SELECT * FROM coupon WHERE end_date >=?";
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
			if (!list.isEmpty()) {
				return list;
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public Collection<Coupon> getCouponByTypeForCustomer(CouponType type, long customerId) throws ApplicationException {
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
			String sql2 = "SELECT * FROM coupon WHERE coup_id=? AND type= ?";
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
				if (!list.isEmpty()) {
					return list;
				}
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public Collection<Coupon> getCouponByTypeForCompany(CouponType type, long companyId) throws ApplicationException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE type=? AND comp_id=? ";
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
			if (!collection.isEmpty()) {
				return collection;
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		if (!collection.isEmpty()) {
			return collection;
		}
		throw new ApplicationException(ErrorTypes.NO_COUPONS);
	}

	@Override
	public boolean couponExists(Long id) throws ApplicationException {
		String sql = "SELECT coup_id FROM coupon WHERE coup_id=?";
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
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}
}

// NOT USED AT THE MOMENT
// public boolean customerOwnsCoupon(Coupon coup, Customer cust) throws
// ApplicationException {
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
// throw new ApplicationException("No coupons were found");
// }
// }

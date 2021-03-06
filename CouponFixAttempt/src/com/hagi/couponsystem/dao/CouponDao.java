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

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COUPON_ALREADY_EXISTS);

		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void fullyRemoveCoupon(Long couponId) throws ApplicationException {
		removeCouponFromCustomerTable(couponId);
		removeCouponFromCouponTable(couponId);
	}
// Old Method
//	@Override
//	public void fullyRemoveCoupon(Long couponId) throws ApplicationException {
//		String sql = "DELETE FROM coupon WHERE coup_id=?";
//		String sql2 = "DELETE FROM customer_coupon WHERE coup_id=?";
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		PreparedStatement preparedStatement2 = null;
//		try {
//			connection = pool.getConnection();
//			preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.setLong(1, couponId);
//			preparedStatement.executeUpdate();
//			preparedStatement2 = connection.prepareStatement(sql2);
//			preparedStatement.setLong(1, couponId);
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
//		} finally {
//			UtilSQLcloser.SQLCloser(preparedStatement);
//			UtilSQLcloser.SQLCloser(preparedStatement2);
//			pool.returnConnection(connection);
//		}
//	}

	@Override
	public void updateCoupon(Coupon coup) throws ApplicationException {

		String sql = "UPDATE coupon SET title=?, start_date=?, end_date=?, amount=?, type=?, message=?, price=?, image=?, comp_id=? WHERE coup_id=?";
		Connection connection = pool.getConnection();
		try (PreparedStatement stmt = connection.prepareStatement(sql);) {

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
			pool.returnConnection(connection);
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
			}
			return coupon;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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
			return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			pool.returnConnection(con);
		}
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
				return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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
	public void removeCouponFromCustomerTable(Long id) throws ApplicationException {

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
	public void removeCouponFromCouponTable(Long id) throws ApplicationException {

		String sql = "DELETE FROM coupon WHERE coup_id=?";
		Connection connection = pool.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COUPON_DOSENT_EXIST);

		} finally {
			pool.returnConnection(connection);
		}

	}

	@Override
	public Collection<Long> getAllExpiredCoupons() throws ApplicationException {

		ArrayList<Long> list = new ArrayList<>();
		Date date1 = new Date(System.currentTimeMillis());
		String sql = "SELECT coup_id FROM coupon WHERE end_date < '" + date1 + "'";
		Connection connection = ConnectionPool.getPool().getConnection();
		// There is no use of a preparedStatement here becuase this method is not used
		// outside the serverside
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {

			while (rs.next()) {
				list.add(rs.getLong("coup_id"));
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.SYSTEM_FAILFURE);

		} finally {
			pool.returnConnection(connection);
		}
		return list;
	}

	@Override
	public Collection<Coupon> getAllCouponsForCompany(Long compId) throws ApplicationException {

		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT * FROM coupon WHERE comp_id=?";
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
			return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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
			return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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
			return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponByDateForCustomer(Date date, long custId) throws ApplicationException {
		String sql = "SELECT coup_id FROM customer_coupon WHERE cust_id=?";
		String sql2 = "SELECT * FROM coupon WHERE coup_id=? AND end_date <= ?";
		Collection<Coupon> list = new ArrayList<Coupon>();
		Collection<Long> collection = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;

		connection = pool.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, custId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				collection.add(resultSet.getLong("coup_id"));
			}
			for (Long long1 : collection) {
				preparedStatement2 = connection.prepareStatement(sql2);
				preparedStatement2.setLong(1, long1);
				preparedStatement2.setDate(2, date);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					Coupon coupon = null;
					coupon = Extractor.extractCouponFromResultSet(resultSet2);
					list.add(coupon);
				}
			}
			return list;
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			UtilSQLcloser.SQLCloser(preparedStatement2);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponByDateForCompany(Date date, long compId) throws ApplicationException {
		String sql = "SELECT * FROM coupon WHERE end_date <=? AND comp_id=?";
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
			return list;
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponByDate(Date date) throws ApplicationException {
		String sql = "SELECT * FROM coupon WHERE end_date <=?";
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
			return list;
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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
			}
			return list;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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
			return collection;
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
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

	@Override
	public boolean companyHasCoupons(Long compId) throws ApplicationException {
		String sql = "SELECT comp_id FROM coupon WHERE comp_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, compId);
			resultSet = preparedStatement.executeQuery();
			return (resultSet.next());

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean customerHasCoupons(Long custId) throws ApplicationException {
		String sql = "SELECT coup_id FROM customer_coupon WHERE cust_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, custId);
			resultSet = preparedStatement.executeQuery();
			return resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.CUSTOMER_DOSENT_EXIST);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponsForCustomer(Long id) throws ApplicationException {
		Collection<Long> idCollection = new ArrayList<Long>();
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT coup_id from customer_coupon WHERE cust_id=?";
		String sql2 = "Select *  FROM coupon where coup_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;

		try {

			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			// Getting the coupon ids and placing them in a coupon object
			while (resultSet.next()) {
				Long num = null;
				num = resultSet.getLong("coup_id");
				idCollection.add(num);
			}
			// placing the rest of the coupon variables into the object
			preparedStatement2 = connection.prepareStatement(sql2);
			for (Long num : idCollection) {
				preparedStatement2.setLong(1, num);
				resultSet2 = preparedStatement2.executeQuery();
				while (resultSet2.next()) {
					Coupon coupon = null;
					coupon = Extractor.extractCouponFromResultSet(resultSet2);
					collection.add(coupon);
				}
			}
			return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			UtilSQLcloser.SQLCloser(preparedStatement2);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponByPriceForCustomer(double price, long customerId) throws ApplicationException {
		Collection<Coupon> collection = new ArrayList<Coupon>();
		Collection<Long> longCollection = new ArrayList<Long>();
		String sql = "SELECT coup_id FROM customer_coupon WHERE cust_id=?";
		String sql2 = "Select *  FROM coupon where coup_id=? AND price<=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;

		try {

			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				long num = resultSet.getLong("coup_id");
				longCollection.add(num);
			}

			preparedStatement2 = connection.prepareStatement(sql2);
			preparedStatement2.setDouble(2, price);
			for (Long long1 : longCollection) {
				ResultSet resultSet2 = null;
				preparedStatement2.setLong(1, long1);
				resultSet2 = preparedStatement2.executeQuery();
				while (resultSet2.next()) {
					Coupon coupon = null;
					coupon = Extractor.extractCouponFromResultSet(resultSet2);
					collection.add(coupon);
				}
			}
			return collection;

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.NO_COUPONS);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			UtilSQLcloser.SQLCloser(preparedStatement2);
			pool.returnConnection(connection);
		}
	}
}

package com.hagi.couponsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Utils.Extractor;
import com.hagi.couponsystem.Utils.UtilSQLcloser;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.exception.dao.CustomerDoesNotExistException;
import com.hagi.couponsystem.exception.dao.DaoException;

public class CompanyDao implements ICompanyDao {
	private ConnectionPool pool = ConnectionPool.getPool();

	public CompanyDao() {
		super();
	}

	@Override
	public void createCompany(Company comp) throws DaoException {
		String sql1 = "INSERT INTO company  (comp_id,name,password,email) VALUES (?,?,?,?)";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql1);) {

			stmt.setLong(1, comp.getId());
			stmt.setString(2, comp.getCompName());
			stmt.setString(3, comp.getPassword());
			stmt.setString(4, comp.getEmail());
			stmt.executeUpdate();

			System.out.println(comp + " has been added");

		} catch (SQLException e) {
			throw new DaoException("Id is already in use or null variables has been placed");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void removeCompany(Long id) throws DaoException {
		String sql = ("DELETE FROM company WHERE comp_id=?");
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setLong(1, id);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new DaoException("Company was not found");

		} finally {
			pool.returnConnection(con);
		}
	}

	@Override
	public void updateCompany(Company comp) throws DaoException {
		String sql = "UPDATE company SET name=?, password=?,email=? WHERE comp_id=?";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, comp.getCompName());
			stmt.setString(2, comp.getPassword());
			stmt.setString(3, comp.getEmail());
			stmt.setLong(4, comp.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new CustomerDoesNotExistException("This customer does not exist");

		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Company readCompany(Long id) throws DaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company = null;
		try {

			connection = pool.getConnection();
			String sql = "SELECT * FROM company WHERE comp_id = ? ";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			company = Extractor.extractCompanyFromResultSet(resultSet);

		} catch (SQLException e) {
			throw new DaoException("No company was found");

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return company;
	}

	@Override
	public Collection<Company> getAllCompanies() throws DaoException {
		Collection<Company> collection = new ArrayList<Company>();
		String sql = "SELECT * FROM company";
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet resultSet = stmt.executeQuery(sql);) {

			while (resultSet.next()) {
				Company other = null;
				other = Extractor.extractCompanyFromResultSet(resultSet);
				collection.add(other);
			}

		} catch (SQLException e) {
			throw new DaoException("There are no companies");

		} finally {
			pool.returnConnection(con);
		}
		return collection;
	}

	@Override
	public Boolean login(Long id, String password) throws DaoException {
		String sql = "SELECT password FROM company WHERE comp_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				System.out.println("Wrong pass");
				return false;
			}
			String str = resultSet.getString("password");
			if (password.equals(str)) {
				return true;
			} else {
				System.out.println("Wrong pass");
				return false;
			}

		} catch (SQLException e) {
			throw new DaoException("Unable to login-Wrong password or Id");

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Boolean companyExists(Long id) throws DaoException {
		ArrayList<Long> list = new ArrayList<>();
		String sql = "SELECT comp_id FROM company WHERE comp_id=?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = pool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				list.add((resultSet.getLong("comp_id")));
			}
			for (Long long1 : list) {
				if (id.equals(long1)) {
					return true;
				}
			}

		} catch (SQLException e) {
			throw new DaoException("Error occurred at companyExists method");
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return false;
	}
}

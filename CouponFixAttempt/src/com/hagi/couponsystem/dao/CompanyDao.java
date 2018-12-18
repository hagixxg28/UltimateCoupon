package com.hagi.couponsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Idao.ICompanyDao;
import com.hagi.couponsystem.Utils.Extractor;
import com.hagi.couponsystem.Utils.UtilSQLcloser;
import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.connectionpool.ConnectionPool;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class CompanyDao implements ICompanyDao {
	private ConnectionPool pool = ConnectionPool.getPool();
	private static CompanyDao instance;

	private CompanyDao() {
		super();
	}

	public static CompanyDao getInstance() {
		if (instance == null) {
			instance = new CompanyDao();
		}
		return instance;
	}

	@Override
	public void createCompany(Company comp) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.COMPANY_ALREADY_EXISTS);
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void removeCompany(Long id) throws ApplicationException {
		String sql = ("DELETE FROM company WHERE comp_id=?");
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setLong(1, id);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);

		} finally {
			pool.returnConnection(con);
		}
	}

	@Override
	public void updateCompany(Company comp) throws ApplicationException {
		String sql = "UPDATE company SET name=?, password=?,email=? WHERE comp_id=?";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, comp.getCompName());
			stmt.setString(2, comp.getPassword());
			stmt.setString(3, comp.getEmail());
			stmt.setLong(4, comp.getId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);

		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Company readCompany(Long id) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);

		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
		return company;
	}

	@Override
	public Collection<Company> getAllCompanies() throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.NO_COMPANIES);

		} finally {
			pool.returnConnection(con);
		}
		return collection;
	}

	@Override
	public Boolean login(Long id, String password) throws ApplicationException {
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
			throw new ApplicationException(ErrorTypes.FAILED_TO_LOGIN);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public Boolean companyExists(Long id) throws ApplicationException {
		String sql = "SELECT comp_id FROM company WHERE comp_id=?";
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
			throw new ApplicationException(ErrorTypes.COMPANY_DOSENT_EXIST);
		} finally {
			UtilSQLcloser.SQLCloser(preparedStatement);
			pool.returnConnection(connection);
		}
	}
}

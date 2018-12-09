package com.hagi.couponsystem.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.hagi.couponsystem.exception.dao.DaoException;

public class UtilSQLcloser {
	public static void SQLCloser(PreparedStatement preparedStatement) throws DaoException {
		try {
			preparedStatement.close();
//			resultSet.close();
		} catch (SQLException e) {
			throw new DaoException("Failed on closing RS or PS");
		}
	}
}

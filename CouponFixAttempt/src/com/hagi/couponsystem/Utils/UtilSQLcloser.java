package com.hagi.couponsystem.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.exceptions.ApplicationException;

public class UtilSQLcloser {
	public static void SQLCloser(PreparedStatement preparedStatement) throws ApplicationException {
		try {
			preparedStatement.close();
//			resultSet.close();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorTypes.FAILED_TO_CLOSE);
		}
	}
}
